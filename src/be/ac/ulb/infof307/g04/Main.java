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
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Class Main, used to lauch the application
 */


public class Main extends Application {

    @FXML
    private ListView<DatabaseArticle> listViewArticles;

    @FXML
    private MenuItem quitButton;

    @FXML
    private  GridPane gridPane;

    private static ArticleManager article_manager;
    private static SourceManager source;

    private ToolBar searchBar;
    private Button closeSearchButton;
    private TextField searchField;
    private Label match_count;

    @FXML
    private MenuItem readArticleImage;
    @FXML
    private MenuItem searchArticleImage;
    @FXML
    private MenuItem copyArticleLinkImage;
    @FXML
    private MenuItem configureSourcesImage;
    @FXML
    private MenuItem configureTagsImage;
    @FXML
    private MenuItem exitAppImage;

    public static void main(String[] _args) {
        article_manager = new ArticleManager("./article_db", "password");
        initDb();
        source = new SourceManager("./article_db");
        launch(_args);
    }

    @Override
    public void start(Stage _primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleList.fxml"));

        _primaryStage.setTitle("DatabaseArticle List");

        Scene scene = new Scene(root);
        _primaryStage.setScene(scene);
        _primaryStage.show();

    }

    @FXML
    public void initialize() throws IOException, ParserConfigurationException, SAXException, ParseException {
        /*
        Initialize the main window -> has a close button, a search bar and display all the articles in the DB
         */
            searchBar = new ToolBar();
            searchBar.setPrefHeight(40.0);
            searchBar.setPrefWidth(200.0);
            GridPane.setConstraints(searchBar, 0, 0);
            closeSearchButton = new Button("Close");
            closeSearchButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    changeSearchBarStatus();
                }
            });
            searchField = new TextField();
            searchField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {

                    ArrayList<DatabaseArticle> articles = article_manager.loadArticles(newValue);
                    displayArticles(articles);
                    match_count.setText(articles.size() + " matches");
                }
            });
            match_count = new Label();
            searchBar.getItems().addAll(closeSearchButton, searchField, match_count);

            listViewArticles.setCellFactory(lv -> new ArticleCell());
            quitButton.setOnAction(e -> Platform.exit());
            source.download(article_manager);
            article_manager.verifyArticles();

            displayArticles(article_manager.loadArticles());

            setHelpImages();
    }

    private void setImage(String s, int i, int i2, MenuItem readArticleImage) {
        ImageView readIcon = new ImageView(new Image(s));
        readIcon.setFitHeight(i);
        readIcon.setFitWidth(i2);
        readArticleImage.setGraphic(readIcon);
    }
    private void setHelpImages() {
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ReadArticle.png", 250, 400, readArticleImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/SearchByTitle.png", 280, 380, searchArticleImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/CopyToClipboard.png", 250, 400, copyArticleLinkImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureSources.png", 380, 530, configureSourcesImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureTags.png", 350, 550, configureTagsImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/Exit.png", 200, 350, exitAppImage);
    }


    @FXML
    public void displayArticles(ArrayList<DatabaseArticle> _articles) {
        /**
         * Display all the valid _articles in the window
         * @param _articles
         *              _articles that haven't been deleted in the DB
         */
        listViewArticles.getItems().clear();
        for (DatabaseArticle item : _articles) {
            listViewArticles.getItems().add(item);
        }
    }

    @FXML
    private void openArticleWindow() {
        /**
         * Method that opens an article when the user click on it
         * @throws Exception : when no article has been selected
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ViewSingleArticle.fxml"));
            ViewSingleArticle controller = new ViewSingleArticle(listViewArticles.getSelectionModel().getSelectedItem());
            loader.setController(controller);
            controller.setArticlesWindows(this);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("DatabaseArticle Reading");
            stage.setScene(new Scene(root));
            stage.show();

        } catch(NullPointerException e){
            showErrorBox("No article selected");
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            showErrorBox("Parser configuration error");
        }catch(IOException e){
            showErrorBox("No article selected");
            e.printStackTrace();
        }catch(SAXException e){
            showErrorBox("SAX Error");
        } catch (ParseException e) {
            showErrorBox("Parse error");
        }
    }

    private void showErrorBox(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(s);

        alert.showAndWait();
    }

    @FXML
    private void copyLinkToClipboard() {
        /**
         * copy the link of the article
         * @throws Exception : when no article has been selected
         */
        try {
            String myString = listViewArticles.getSelectionModel().getSelectedItem().getLink();
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (Exception e) {
            showErrorBox("Error while copying link to clipboard");
        }
    }

    public void openWindow(FXMLLoader loader, String title, String window_title){
        try {
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            showErrorBox("Error while opening "+ window_title + " window!");
        }
    }

    @FXML
    public void openSourceWindow(ActionEvent _actionEvent) {
        /**
         * Opens the sources window
         * @throws Exception
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/SourceMenu.fxml"));
            SourceMenu controller = new SourceMenu();
            loader.setController(controller);
            openWindow(loader, "Choose sources", "source");
        } catch (Exception e) {
            showErrorBox("Error while opening source window!");
        }
    }

    public void openTagWindow(ActionEvent _actionEvent) {
        /*
        Open the tag window
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/TagMenu.fxml"));
            TagMenu controller = new TagMenu();
            loader.setController(controller);
            openWindow(loader, "Manage tags", "tag");
        } catch (Exception e) {
            showErrorBox("Error while opening the tag window!");
        }
    }
    @FXML
    public void changeSearchBarStatus(){
        /*
        If the search bar is available or not. When you close it, it loads all the articles again
         */
        if (gridPane.getChildren().indexOf(searchBar) == -1) {
            gridPane.getChildren().add(searchBar);
            match_count.setText("");
        } else {
            gridPane.getChildren().remove(searchBar);
            displayArticles(article_manager.loadArticles());
        }
    }

    private static void initDb() {
        /*
        Initialize all the tags and the sources
         */
        initTags();
        initSources();
    }

    private static void initTags() {
        /*
        Initialize all the tags
         */
        String[] tags = {"Business", "Default", "Entertainment", "Health", "Science", "Sports", "Technology"};
        TagManager tagManager = new TagManager("./article_db", "password");
        DatabaseTag tag = new DatabaseTag();
        for(int i = 0; i < tags.length; i++){
            tag.setName(tags[i]);
            tagManager.addTag(tag);
        }
    }

    private static void initSources() {
        /*
        Initialize all the sources
         */
        SourceManager sourceManager = new SourceManager("./article_db");
        ArrayList<DatabaseSource> sources = new ArrayList<>();
        sources.add(new DatabaseSource("The Verge", "https://www.theverge.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("BBC world news", "http://feeds.bbci.co.uk/news/world/rss.xml"));
        sources.forEach(sourceManager::addSource);
    }
};