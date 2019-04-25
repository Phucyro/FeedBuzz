package be.ac.ulb.infof307.g04;


import be.ac.ulb.infof307.g04.controller.*;
import be.ac.ulb.infof307.g04.model.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

/**
 * Class Main, used to lauch the application
 */


public class Main extends Application {

    @FXML
    private ListView<Article> list_view_articles;

    @FXML
    private MenuItem quit_button;

    @FXML
    private  GridPane grid_pane;

    private static ArticleManager article_manager;
    private static SourceManager source;

    private ToolBar search_bar;
    private Button close_search_button;
    private TextField search_field;
    private Label match_count;

    public static void main(String[] args) {
        article_manager = new ArticleManager("./article_db", "password");
        init_db();
        source = new SourceManager("./article_db");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleList.fxml"));

        primaryStage.setTitle("Fenêtre principale");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    public void initialize() {
        /*
        Initialize the main window -> has a close button, a search bar and display all the articles in the DB
         */
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
        source.download(article_manager);
        article_manager.verify_articles();

        display_articles(article_manager.load_articles());
    }

    @FXML
    public void display_articles(ArrayList<Article> articles) {
        /**
         * Display all the valid articles in the window
         * @param articles
         *              articles that haven't been deleted in the DB
         */
        list_view_articles.getItems().clear();
        for (Article item : articles) {
            list_view_articles.getItems().add(item);
            System.out.println(item.toString());
        }
    }

    @FXML
    private void open_article_window() {
        /**
         * Method that opens an article when the user click on it
         * @throws Exception : when no article has been selected
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ViewSingleArticle.fxml"));
            ViewSingleArticle controller = new ViewSingleArticle(list_view_articles.getSelectionModel().getSelectedItem());
            System.out.println("Ouverture de l'article");
            loader.setController(controller);
            controller.set_articles_windows(this);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Aucun article n'a été sélectionné");
        }
    }

    @FXML
    private void copy_link_to_clipboard() {
        /**
         * copy the link of the article
         * @throws Exception : when no article has been selected
         */
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
        /**
         * Opens the sources of the article
         * @throws Exception : when no article has been selected
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/SourceMenu.fxml"));
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
        /*
        If the search bar is available or not. When you close it, it loads all the articles again
         */
        if (grid_pane.getChildren().indexOf(search_bar) == -1) {
            grid_pane.getChildren().add(search_bar);
            match_count.setText("");
        } else {
            grid_pane.getChildren().remove(search_bar);
            display_articles(article_manager.load_articles());
        }
    }

    @FXML
    public void open_help_window(ActionEvent actionEvent) {
        /**
         * Opens the help section
         * @throws Exception
         */
        try {
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init_db() {
        /*
        Initialize all the tags and the sources
         */
        init_tags();
        init_sources();
    }

    private static void init_tags() {
        /*
        Initialize all the tags
         */
        String[] tags = {"Business", "Default", "Entertainment", "Health", "Science", "Sports", "Technology"};
        TagManager tagManager = new TagManager("./article_db", "password");
        DatabaseTag tag = new DatabaseTag();
        for(int i = 0; i < tags.length; i++){
            tag.setName(tags[i]);
            tagManager.add_tag(tag);
        }
    }

    private static void init_sources() {
        /*
        Initialize all the sources
         */
        SourceManager sourceManager = new SourceManager("./article_db");
        ArrayList<DatabaseSource> sources = new ArrayList<>();
        sources.add(new DatabaseSource("The Verge", "https://www.theverge.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("BBC world news", "http://feeds.bbci.co.uk/news/world/rss.xml"));
        sources.forEach(sourceManager::add_source);
    }

    public void open_tag_window(ActionEvent actionEvent) {
        /*
        Open the tag window
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/TagMenu.fxml"));
            TagMenu controller = new TagMenu();
            loader.setController(controller);
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

};