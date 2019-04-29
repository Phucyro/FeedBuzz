package be.ac.ulb.infof307.g04;


import be.ac.ulb.infof307.g04.controller.*;
import be.ac.ulb.infof307.g04.model.*;
import javafx.application.Application;
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


public class ViewListArticles extends Application {


    @FXML
    private ListView<DatabaseArticle> listViewArticles;

    @FXML
    private MenuItem QuitButton;

    @FXML
    private  GridPane GridPane;

    private static ArticleManager articleManager;
    private static SourceManager source;

    private ToolBar searchBar;
    private Button CloseSearchButton;
    private TextField searchField;
    private Label matchCount;
    private String dbPath;
    private String password;
    private ArrayList <Stage> stageArrayList = new ArrayList<Stage>();

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
    @FXML
    private Stage primaryStage;


    public ViewListArticles(String _pathToDB, String _password){
        dbPath = _pathToDB;
        password = _password;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleList.fxml"));
        primaryStage.setTitle("Fenêtre principale");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

    }

    /**
     * Initialize the view list window: it has a close button, a search bar and display all the articles in the DB
     */
    @FXML
    public void initialize() {

        articleManager = new ArticleManager(dbPath, password);
        init_db();
        source = new SourceManager(dbPath, password);
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

                ArrayList<DatabaseArticle> articles = articleManager.loadArticles(newValue);
                displayArticles(articles);
                matchCount.setText(articles.size() + " matches");
            }
        });
        matchCount = new Label();
        searchBar.getItems().addAll(CloseSearchButton, searchField, matchCount);

        listViewArticles.setCellFactory(lv -> new ArticleCell());
        setHelpImages();
//        QuitButton.setOnAction(e -> Platform.exit());
        if (InternetTester.testInternet()) {
            try {
                source.download(articleManager);

                articleManager.verifyArticles();
            } catch (Exception ignored) {

            }
        }else{
            //cas sans internet
        }


        displayArticles(articleManager.loadArticles());

    }


    /**
     * disconnected the user
     */
    @FXML
    public void disconnect() {
        for (int i = 0; i < stageArrayList.size(); i++) {
            stageArrayList.get(i).close();
        }


    }

    @FXML
    public void relaunch() throws Exception {
        disconnect();
        Main myMain = new Main();
        myMain.start(new Stage());
    }

    /**
     * Display all the valid _articles in the window
     * @param _articles
     *              _articles that haven't been deleted in the DB
     */
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

    /**
     * @param _path path of the image
     * @param _height height of the image
     * @param _width width of the image
     * @param _readArticleImage place to put the image
     */
    private void setImage(String _path, int _height, int _width, MenuItem _readArticleImage) {
        ImageView readIcon = new ImageView(new Image(_path));
        readIcon.setFitHeight(_height);
        readIcon.setFitWidth(_width);
        _readArticleImage.setGraphic(readIcon);
    }

    /**
     * load the images of the help menu
     */
    private void setHelpImages() {
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ReadArticle.png", 250, 400, readArticleImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/SearchByTitle.png", 280, 380, searchArticleImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/CopyToClipboard.png", 250, 400, copyArticleLinkImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureSources.png", 380, 530, configureSourcesImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureTags.png", 350, 550, configureTagsImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/Exit.png", 200, 350, exitAppImage);
    }

    /**
     * Method that opens an article when the user click on it
     * @throws Exception : when no article has been selected
     */
    @FXML
    private void openArticleWindow() {
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
        }catch(ParserConfigurationException e){
            showErrorBox("Parser configuration error");
        }catch(IOException e){
            showErrorBox("No article selected");
        }catch(SAXException e){
            showErrorBox("SAX Error");
        } catch (ParseException e) {
            showErrorBox("Parse error");
        }
    }

    /**
     * show an error box with a message
     * @param _errorMessage error message to display
     */
    private void showErrorBox(String _errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(_errorMessage);

        alert.showAndWait();
    }

    /**
     * copy the link of the article
     */
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

    /**
     * @param _loader _loader
     * @param _titleWindow title of the window
     * @param _title parameter use for the error message
     */
    public void openWindow(FXMLLoader _loader, String _titleWindow, String _title){
        try {
            Parent root = (Parent) _loader.load();

            Stage stage = new Stage();
            stage.setTitle(_titleWindow);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            showErrorBox("Error while opening "+ _title + " window!");
        }
    }

    /**
     * Open the source window
     */
    @FXML
    public void openSourceWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/SourceMenu.fxml"));
            SourceMenu controller = new SourceMenu(dbPath, password);
            loader.setController(controller);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  If the search bar is available or not. When you close it, it loads all the articles again
     */
    @FXML
    public void changeSearchBarStatus(){
        if (GridPane.getChildren().indexOf(searchBar) == -1) {
            GridPane.getChildren().add(searchBar);
            matchCount.setText("");
        } else {
            GridPane.getChildren().remove(searchBar);
            displayArticles(articleManager.loadArticles());
        }
    }

    /**
     * Initialize all the tags and the sources
     */
    private void init_db() {
        init_tags();
        init_sources();
    }

    /**
     * Initialize all the tags
     */
    private void init_tags() {
        String[] tags = {"Business", "Default", "Entertainment", "Health", "Science", "Sports", "Technology"};
        TagManager tagManager = new TagManager(dbPath, password);
        DatabaseTag tag = new DatabaseTag();
        for(int i = 0; i < tags.length; i++){
            tag.setName(tags[i]);
            tagManager.addTag(tag);
        }
    }

    /**
     * Initialize all the sources
     */
    private void init_sources() {
        SourceManager sourceManager = new SourceManager(dbPath, password);
        ArrayList<DatabaseSource> sources = new ArrayList<>();
        sources.add(new DatabaseSource("The Verge", "https://www.theverge.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("BBC world news", "http://feeds.bbci.co.uk/news/world/rss.xml"));
        sources.forEach(sourceManager::addSource);
    }

    /**
     * open the tag window
     */
    public void openTagWindow(ActionEvent _actionEvent) {
        /*
        Open the tag window
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/TagMenu.fxml"));
            TagMenu controller = new TagMenu(dbPath, password);
            loader.setController(controller);
            openWindow(loader, "Manage tags", "tag");
        } catch (Exception e) {
            showErrorBox("Error while opening the tag window!");
        }
    }

};