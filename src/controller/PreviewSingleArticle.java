package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class PreviewSingleArticle extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private Button show_article;
    @FXML
    private Button open_linkl;
    @FXML
    private Button copy_link;
    @FXML
    private Button delete_article;

    @FXML
    private Label title;
    @FXML
    private TextArea summary;


    public void set_title(String titre) {
        this.title.setText(titre);
    }


    public void set_summary(String texte) {
        this.summary.setText(texte);
    }

    @FXML
    private void show() {
        //code for showing article

    }

    @FXML
    private void open() {
        //code to open article's link

    }

    @FXML
    private void copy() {
        //code to copy article's link

    }

    @FXML
    private void remove() {
        //code to remove local's article
    }

    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PreviewSingleArticle.class.getResource("/view/PreviewSingleArticle.fxml"));
        try {
            AnchorPane conteneurPrincipal;
            conteneurPrincipal = loader.load();
            Scene scene = new Scene(conteneurPrincipal);
            primaryStage.setTitle("Fenetre preview");

            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
