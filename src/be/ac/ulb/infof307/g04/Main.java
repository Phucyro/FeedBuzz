package be.ac.ulb.infof307.g04;

import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.ArticleManager;
import model.DatabaseTag;
import model.TagManager;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;


public class Main extends Application {

    @FXML
    private ListView<Article> list_view_articles;

    @FXML
    private MenuItem quit_button;

    @FXML
    private  GridPane grid_pane;

    private static ArticleManager article_manager;

    private ToolBar search_bar;
    private Button close_search_button;
    private TextField search_field;
    private Label match_count;

    public static void main(String[] args) {
        article_manager = new ArticleManager("./article_db", "password");
        init_db();
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
        search_bar = new ToolBar();
        search_bar.setPrefHeight(40.0);
        search_bar.setPrefWidth(200.0);
        GridPane.setConstraints(search_bar, 0, 0);
        close_search_button = new Button("Close");
        close_search_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                change_search_bar_status();
            }
        });
        search_field = new TextField();
        search_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                ArrayList<Article> articles = article_manager.load_articles(newValue);
                display_articles(articles);
                match_count.setText(articles.size() + " matches");
            }
        });
        match_count = new Label();
        search_bar.getItems().addAll(close_search_button, search_field, match_count);

        list_view_articles.setCellFactory(lv -> new ArticleCell());
        quit_button.setOnAction(e -> Platform.exit());

        display_articles(article_manager.load_articles());
    }

    @FXML
    public void display_articles(ArrayList<Article> articles) {
        list_view_articles.getItems().clear();
        for (Article item : articles) {
            list_view_articles.getItems().add(item);
        }
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
            System.out.println(e);
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

    @FXML
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
            e.printStackTrace();
        }
    }

    @FXML
    public void change_search_bar_status(){
        if (grid_pane.getChildren().indexOf(search_bar) == -1) {
            grid_pane.getChildren().add(search_bar);
            match_count.setText("");
        } else {
            grid_pane.getChildren().remove(search_bar);
            display_articles(article_manager.load_articles());
        }
    }

    private static void init_db() {
        init_tags();
        init_sources();
    }

    private static void init_tags() {
        String[] tags = {"Business", "Default", "Entertainment", "Health", "Science", "Sports", "Technology"};
        TagManager tagManager = new TagManager("./article_db", "password");
        DatabaseTag tag = new DatabaseTag();
        for(int i = 0; i < tags.length; i++){
            tag.setName(tags[i]);
            tagManager.add_tag(tag);
        }
    }

    private static void init_sources() {

    }

};