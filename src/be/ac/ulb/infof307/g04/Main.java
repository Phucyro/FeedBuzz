package be.ac.ulb.infof307.g04;

import controller.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Main extends Application {

    @FXML
    private ListView<Article> list_view_articles;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ArticleList.fxml"));
        primaryStage.setTitle("Fenêtre principale");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    @FXML
    public void initialize() {
        list_view_articles.setCellFactory(lv -> new ArticleCell());
        ParserRss parser = new ParserRss();
        ArrayList<Article> articles = parser.parse("https://www.theverge.com/rss/index.xml");
        for (Article item : articles) {
            showArticleImage(item);
        }
    }

    @FXML
    public void showArticleImage(Article article) {
        list_view_articles.getItems().add(article);
        //image_test.setImage(image);
    }

    @FXML
    private void open_article_window() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ViewSingleArticle.fxml"));
            ViewSingleArticle controller = new ViewSingleArticle(list_view_articles.getSelectionModel().getSelectedItem());
            System.out.println("Ouverture de l'article");
            loader.setController(controller);
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Veuillez selectionner un article");
        }
    }

    @FXML
    private void copy_link_to_clipboard() {
        try {
            String myString = list_view_articles.getSelectionModel().getSelectedItem().getLink();
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (Exception e) {
            System.out.println("Veuillez selectionner un article");
        }
    }

    public void open_source_window(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SourceMenu.fxml"));
            SourceMenu controller = new SourceMenu();
            loader.setController(controller);
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
        }
    }
};