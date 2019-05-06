package be.ac.ulb.infof307.g04.view;


import be.ac.ulb.infof307.g04.Main;
import be.ac.ulb.infof307.g04.controller.ArticleCell;
import be.ac.ulb.infof307.g04.controller.InternetTester;
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
import javafx.scene.layout.VBox;
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


public class ArticleListController extends Application {


    @FXML
    private ListView<DatabaseArticle> listViewArticles;

    @FXML
    private MenuItem QuitButton;

    @FXML
    private VBox VBox;

    private static ArticleManager article_manager;
    private static SourceManager source;

    private ToolBar searchBar;
    private Button CloseSearchButton;
    private TextField searchField;
    private Label match_count;
    private String dbPath;
    private String password;
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


    public ArticleListController(String _pathToDB, String _password){
        dbPath = _pathToDB;
        password = _password;
    }

    @Override
    public void start(Stage primaryStage) throws Exception { }


    /**
     * Initialize the main window: has a close button, a search bar and display all the articles in the DB
     */
    @FXML
    public void initialize() {

        article_manager = new ArticleManager(dbPath, password);
        init_db();
        source = new SourceManager(dbPath, password);
        searchBar = new ToolBar();
        init_searchBar(40, 200);

        CloseSearchButton = new Button("Close");
        init_closeSearchButton();

        searchField = new TextField();
        init_searchField();
        match_count = new Label();
        searchBar.getItems().addAll(CloseSearchButton, searchField, match_count);

        listViewArticles.setCellFactory(lv -> new ArticleCell());
        setHelpImages();

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


    /**
     * Shuts off all windows of active session.
     */
    @FXML
    public void disconnect() {
        for (int i = 0; i < stageArrayList.size(); i++) {
            stageArrayList.get(i).close();
        }

        mainStage.close();
    }

    /**
     * relaunches the application after the disconnecting of windows
     * goes to logging screen
     * @throws Exception
     */
    @FXML
    public void relaunch() throws Exception {
        disconnect();
        Main mymain = new Main();
        mymain.start(new Stage());
    }

    /**
     * Display all the valid _articles in the window
     * @param _articles
     *              _articles that haven't been deleted in the DB
     */
    @FXML
    public void displayArticles(ArrayList<DatabaseArticle> _articles) {
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

    public void setMainStage(Stage _stage){
        mainStage = _stage;
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
            FXMLLoader loader = new FXMLLoader(ViewSingleArticleController.class.getResource("ViewSingleArticle.fxml"));
            DatabaseArticle articleToRead = listViewArticles.getSelectionModel().getSelectedItem();
            ViewSingleArticleController controller = new ViewSingleArticleController(articleToRead, dbPath, password);
            loader.setController(controller);
            controller.setArticlesWindows(this);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle(articleToRead.getTitle());
            setStage(root, stage);
            stageArrayList.add(stage);

        }catch(NullPointerException e){
            showErrorBox("No article selected");
        }catch(ParserConfigurationException e){
            showErrorBox("Parser configuration error");
        }catch(IOException e){
            showErrorBox("No article selected");
        }catch(SAXException e){
            showErrorBox("SAX Error");
        }catch (ParseException e) {
            showErrorBox("Parse error");
        }
    }

    /**
     * show a eroor box with a message
     * @param _errorMessage the error message to print
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
     * @param _title_window title of the window
     * @param _title parameter use for the error message
     */
    public void openWindow(FXMLLoader _loader, String _title_window, String _title){
        try {
            Parent root = (Parent) _loader.load();
            Stage stage = new Stage();
            stage.setTitle(_title_window);
            setStage(root, stage);
        }
        catch (Exception e) {
            showErrorBox("Error while opening "+ _title + " window!");
        }
    }


    /**
     * @param actionEvent opens the SourceWindow (download settings)
     */
    @FXML
    public void openSourceWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(SourceMenuController.class.getResource("SourceMenu.fxml"));
            SourceMenuController controller = new SourceMenuController(dbPath, password);
            loader.setController(controller);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            setStage(root, stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStage(Parent root, Stage stage) {
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     *  If the search bar is available or not. When you close it, it loads all the articles again
     */
    @FXML
    public void changeSearchBarStatus(){
        if (VBox.getChildren().indexOf(searchBar) == -1) {
            VBox.getChildren().add(searchBar);
            match_count.setText("");
        } else {
            VBox.getChildren().remove(searchBar);
            displayArticles(article_manager.loadArticles());
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
     * Initialize searchbar parameters
     * @param height
     * @param width
     */
    private void init_searchBar(int height, int width){

        searchBar.setPrefHeight(height);
        searchBar.setPrefWidth(width);
        GridPane.setConstraints(searchBar, 0, 0);
    }

    /**
     * Initialize "close" button from search bar
     */
    private void init_closeSearchButton() {
        CloseSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeSearchBarStatus();
            }
        });
    }

    /**
     * Initialize searchField
     */
    private void init_searchField() {
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                ArrayList<DatabaseArticle> articles = article_manager.loadArticles(newValue);
                displayArticles(articles);
                match_count.setText(articles.size() + " matches");
            }
        });
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
        sources.add(new DatabaseSource("JeuxVideo.com", "http://www.jeuxvideo.com/rss/rss.xml", "Technology"));
        sources.add(new DatabaseSource("Vox", "https://www.vox.com/rss/world/index.xml"));
        sources.add(new DatabaseSource("CNN Money", "http://rss.cnn.com/rss/money_topstories.rss","Business"));
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
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/TagMenuController.fxml"));
            FXMLLoader loader = new FXMLLoader(TagMenuController.class.getResource("TagMenu.fxml"));
            TagMenuController controller = new TagMenuController(dbPath, password);
            loader.setController(controller);
            openWindow(loader, "Manage tags", "tag");
        } catch (Exception e) {
            showErrorBox("Error while opening the tag window!");
        }
    }

};
