package be.ac.ulb.infof307.g04;


import be.ac.ulb.infof307.g04.controller.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    /**
     * @param primaryStage Opens logging screen
     * @throws Exception caused by the fxml loader if the file doesn't exist
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/LoginRegister.fxml"));
        ViewLoginRegister controller = new ViewLoginRegister();
        loader.setController(controller);
        Parent loginRoot = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(loginRoot));
        stage.show();
    }

}
