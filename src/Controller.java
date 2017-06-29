    package sample;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.Initializable;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;


    import java.io.*;
    import java.io.IOException;
    import java.net.URL;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.security.*;
    import java.util.Base64;
    import java.util.ResourceBundle;
    import javax.crypto.Cipher;
    import javax.crypto.CipherOutputStream;
    import javax.crypto.KeyGenerator;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.SecretKeySpec;

    public class Controller implements Initializable{
        public javafx.scene.control.TextField textfield;
        public Label label1;
        public Button button1;
        public Button button2;
        public Button button3;
        public Button button4;
        public Label label2;
        private File file;
        private final FileChooser fileChooser = new FileChooser();
        final private static int mBlocksize;
        private static SecretKey secretKey;
        private static String res;
        static {
            mBlocksize = 128;
            secretKey = null;
            res="0";
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                secretKey = kgen.generateKey();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }


        @FXML
        protected void handle1(ActionEvent event) {

            chooseFile();
            if (file==null){return;}
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save encrypted File");
            fileChooser.setInitialDirectory(new File(file.getParent()));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter((getFileExtension(file).toUpperCase()), "*."+getFileExtension(file)),
                    new FileChooser.ExtensionFilter("All files", "*.*")
            );


            /*//Randomize number with Random From security
            SecureRandom srand = new SecureRandom();
            long randLong = (srand.nextInt()%10000000);*/
            File outfile = fileChooser.showSaveDialog(new Stage());
            if (outfile==null){return;}
            /*AES Encryption*/
            try{
                InputStream fis = new FileInputStream(file);
                int read = 0;
                if (!outfile.exists()) {
                    outfile.createNewFile();
                }
                FileOutputStream encfos = new FileOutputStream(outfile);
                Cipher encipher = Cipher.getInstance("AES");
                encipher.init(Cipher.ENCRYPT_MODE, secretKey);
                CipherOutputStream cipheoutstream = new CipherOutputStream(encfos, encipher);
                byte[] block = new byte[mBlocksize];
                while ((read = fis.read(block,0,mBlocksize)) != -1) {
                    cipheoutstream.write(block,0, read);
                }
                cipheoutstream.close();
                fis.close();
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        protected void handle2 (ActionEvent event){
            //AES Crypto decrypted
            try{
                chooseFile();
                if (file==null){

                    return;}
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save decrypted File");
                fileChooser.setInitialDirectory(new File(file.getParent()));
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter((getFileExtension(file).toUpperCase()), "*."+getFileExtension(file)),
                        new FileChooser.ExtensionFilter("All files", "*.*")
                );
                File outfile = fileChooser.showSaveDialog(new Stage());
                if (outfile==null){ label1.setText("Choose File to Decrypt");return;}
                InputStream fis = new FileInputStream(file);
                int read = 0;
                if (!outfile.exists())
                    outfile.createNewFile();

                FileOutputStream encfos = new FileOutputStream(outfile);

                Cipher encipher = Cipher.getInstance("AES");
                encipher.init(Cipher.DECRYPT_MODE, secretKey);
                CipherOutputStream cipheoutstream = new CipherOutputStream(encfos, encipher);

                byte[] block = new byte[mBlocksize];
                while ((read = fis.read(block,0,mBlocksize)) != -1) {
                    cipheoutstream.write(block,0, read);
                }
                cipheoutstream.close();
                fis.close();
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                e.printStackTrace();
            }

        }


        public void handle3(ActionEvent event) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save KEY");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter(("KEY"), "*.key")
                );
                File outfile = fileChooser.showSaveDialog(new Stage());
                if (outfile == null) {
                    return;
                }
                int read = 0;
                if (!outfile.exists()) {
                    outfile.createNewFile();
                }

                FileOutputStream encfos = new FileOutputStream(outfile);

                encfos.write(secretKey.getEncoded());

                encfos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void chooseFile(){
            file = fileChooser.showOpenDialog(new Stage());
        }

        public void handle4(ActionEvent event) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose KEY");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter(("KEY"), "*.key")
                );
                File infile = fileChooser.showOpenDialog(new Stage());
                if (infile == null) {
                    return;
                }
                Path path = Paths.get(infile.getAbsolutePath());
                byte[] encodedKey = Files.readAllBytes(path);
                secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
                textfield.setText(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private static String getFileExtension(File file) {
            String fileName = file.getName();
            if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                return fileName.substring(fileName.lastIndexOf(".")+1);
            else return "";
        }


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            textfield.setText(Base64.getEncoder().encodeToString(secretKey.getEncoded()));

           // button1.setOnAction(event -> {textfield.setText("DGDHDJ");});
        }

        public void genereteNewKey(ActionEvent event) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                secretKey = kgen.generateKey();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            textfield.setText(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        }
    }
