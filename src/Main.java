package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class  Main extends Application {


    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.getIcons().add(new Image("sample/Key-PNG-Clipart.png"));
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(230);
        primaryStage.setMaxWidth(400);
        primaryStage.setMaxHeight(230);
        primaryStage.setTitle("Simple File Encrypt/Decrypt App");
        primaryStage.setScene(new Scene(root, 400, 230));
        primaryStage.show();
    }
}
