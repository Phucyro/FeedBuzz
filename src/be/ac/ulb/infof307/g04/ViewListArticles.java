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
import javafx.stage.Window;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


public class ViewListArticles extends Application {


    @FXML
    private ListView<DatabaseArticle> listViewArticles;

    @FXML
    private MenuItem QuitButton;

    @FXML
    private  GridPane GridPane;

    private static ArticleManager article_manager;
    private static SourceManager source;

    private ToolBar searchBar;
    private Button CloseSearchButton;
    private TextField searchField;
    private Label match_count;
    private String db_path;
    private ArrayList <Stage> stageArrayList = new ArrayList<Stage>();
    private Stage mainStage;

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


    public ViewListArticles(String path_to_db){
        // article_manager = new ArticleManager("./test.db","abcdefgh");
        db_path = new String(path_to_db);
    }

    @Override
    public void start(Stage primaryStage) throws Exception { }

    @FXML
    public void initialize() {

        article_manager = new ArticleManager(db_path, "password");
        init_db();
        source = new SourceManager(db_path);
        searchBar = new ToolBar();
        searchBar.setPrefHeight(40.0);
        searchBar.setPrefWidth(200.0);
        GridPane.setConstraints(searchBar, 0, 0);
        CloseSearchButton = new Button("Close");
        CloseSearchButton.setOnAction(new EventHandler<ActionEvent>() {
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
        searchBar.getItems().addAll(CloseSearchButton, searchField, match_count);

        listViewArticles.setCellFactory(lv -> new ArticleCell());
        setHelpImages();
//        QuitButton.setOnAction(e -> Platform.exit());
        if (InternetTester.testInternet()) {
            try {
                source.download(article_manager);

                article_manager.verifyArticles();
            } catch (Exception ignored) {

            }
        }else{
            //cas sans internet
        }


        displayArticles(article_manager.loadArticles());

    }


    @FXML
    public void disconnect() {
        for (int i = 0; i < stageArrayList.size(); i++) {
            stageArrayList.get(i).close();
        }
        mainStage.close();
    }

    @FXML
    public void relaunch() throws Exception {
        disconnect();
        Main mymain = new Main();
        mymain.start(new Stage());
    }

    @FXML
    public void displayArticles(ArrayList<DatabaseArticle> _articles) {
        /**
         * Display all the valid _articles in the window
         * @param _articles
         *              _articles that haven't been deleted in the DB
         */
        listViewArticles.getItems().setAll(_articles);
    }

    private void setImage(String s, int i, int i2, MenuItem readArticleImage) {
        ImageView readIcon = new ImageView(new Image(s));
        readIcon.setFitHeight(i);
        readIcon.setFitWidth(i2);
        readArticleImage.setGraphic(readIcon);
    }
    public void setMainStage(Stage stage){
        mainStage = stage;

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
    private void openArticleWindow() {
        /**
         * Method that opens an article when the user click on it
         * @throws Exception : when no article has been selected
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ViewSingleArticle.fxml"));
            DatabaseArticle articleToRead = listViewArticles.getSelectionModel().getSelectedItem();
            ViewSingleArticle controller = new ViewSingleArticle(articleToRead);
            loader.setController(controller);
            controller.setArticlesWindows(this);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle(articleToRead.getTitle());
            stage.setScene(new Scene(root));
            stage.show();
            stageArrayList.add(stage);

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
    public void openSourceWindow(ActionEvent actionEvent) {
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
    public void changeSearchBarStatus(){
        if (GridPane.getChildren().indexOf(searchBar) == -1) {
            GridPane.getChildren().add(searchBar);
            match_count.setText("");
        } else {
            GridPane.getChildren().remove(searchBar);
            displayArticles(article_manager.loadArticles());
        }
    }

    private void init_db() {
        init_tags();
        init_sources();
    }

    private void init_tags() {
        String[] tags = {"Business", "Default", "Entertainment", "Health", "Science", "Sports", "Technology"};
        TagManager tagManager = new TagManager(db_path, "password");
        DatabaseTag tag = new DatabaseTag();
        for(int i = 0; i < tags.length; i++){
            tag.setName(tags[i]);
            tagManager.addTag(tag);
        }
    }

    private void init_sources() {
        SourceManager sourceManager = new SourceManager(db_path);
        ArrayList<DatabaseSource> sources = new ArrayList<>();
        sources.add(new DatabaseSource("The Verge", "https://www.theverge.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("BBC world news", "http://feeds.bbci.co.uk/news/world/rss.xml"));
        sources.forEach(sourceManager::addSource);
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

};