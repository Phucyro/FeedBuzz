package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ArticleList extends Application{

    @FXML
    private ImageView image_test;

    @FXML
    private Label title;

    @FXML
    private Label tags;

    @FXML
    private Label keywords;

    @FXML
    private Label source;

    @FXML
    private Label localisation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/view/ArticleList.fxml"));
        primaryStage.setTitle("Fenêtre principale");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }



    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/pictures/moon.jpg"));
        showArticleImage(image);
        showArticleTitle();
        showArticleTags();
        showArticleKeywords();
        showArticleSources();
        showArticleLocalisation();
    }

    @FXML
    public void showArticleImage(Image image) {
        image_test.setImage(image);
    }

    @FXML
    private void showArticleTitle() {
        title.setText("Essai titre");
    }

    @FXML
    private void showArticleTags() {
    }

    @FXML
    private void showArticleKeywords() {
    }

    @FXML
    private void showArticleSources() {
    }

    @FXML
    private void showArticleLocalisation() {
    }
}