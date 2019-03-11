package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;


public class ArticleList extends Application{

    @FXML
    private ListView<Article> list_view_articles;

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
        list_view_articles.setCellFactory(lv -> new ArticleCell());
        Article test = new Article();
        test.setLink("http://test.test");
        test.setTitle("Article de test");
        test.setDescription("Description de test");
        showArticleImage(test);
    }

    @FXML
    public void showArticleImage(Article article) {
        list_view_articles.getItems().add(article);
        //image_test.setImage(image);
    }
}