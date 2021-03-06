package be.ac.ulb.infof307.g04.view;


import be.ac.ulb.infof307.g04.Main;
import be.ac.ulb.infof307.g04.controller.ArticleCell;
import be.ac.ulb.infof307.g04.controller.HTMLArticleDownloader;
import be.ac.ulb.infof307.g04.controller.InternetTester;
import be.ac.ulb.infof307.g04.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


public class ArticleListController extends Application {


    public static final int SEARCHBAR_HEIGHT = 40;
    public static final int SEARCHBAR_WIDTH = 200;
    private static ArticleManager articleManager;
    private static SourceManager source;
    private final String dbPath;
    private final String password;
    private final ArrayList<Stage> stageArrayList = new ArrayList<>();
    @FXML
    private ListView<DatabaseArticle> listViewArticles;
    @FXML
    private MenuItem QuitButton;
    @FXML
    private VBox VBox;
    private ToolBar searchBar;
    private Button CloseSearchButton;
    private TextField searchField;
    private Label match_count;
    private Stage mainStage;
    @FXML
    private MenuItem readArticleImage;
    @FXML
    private MenuItem searchArticleImage;
    @FXML
    private MenuItem copyArticleLinkImage;
    @FXML
    private MenuItem suggestionImage;
    @FXML
    private MenuItem configureSourcesImage;
    @FXML
    private MenuItem configureTagsImage;
    @FXML
    private MenuItem exitAppImage;


    ArticleListController(String _pathToDB, String _password) {
        dbPath = _pathToDB;
        password = _password;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue) {
                articleManager = new ArticleManager(dbPath, password);
                displayArticles(articleManager.loadArticles());
            }
        });
    }


    /**
     * Initialize the main window: has a close button, a search bar and display all the articles in the DB
     */
    @FXML
    public void initialize() {
        articleManager = new ArticleManager(dbPath, password);
        source = new SourceManager(dbPath, password);
        initJavaFX();
        downloadArticles();
        displayArticles(articleManager.loadArticles());

    }

    /**
     * download articles and show an error box if necessary
     */
    private void downloadArticles() {
        if (InternetTester.testInternet()) {
            try {
                source.download(articleManager);

            } catch (ParserConfigurationException e) {
                MessageBoxes.showErrorBox("Parser Configuration exception");
            } catch (ParseException e) {
                MessageBoxes.showErrorBox("Parse exception");
            } catch (SAXException e) {
                MessageBoxes.showErrorBox("SAX Exception");
            } catch (IOException e) {
                MessageBoxes.showErrorBox("IO Exception");
            } catch (org.json.simple.parser.ParseException e) {
                MessageBoxes.showErrorBox("Json Parse exception");
            }
        } else {
            MessageBoxes.showErrorBox("Pas d'internet");
        }
    }

    /**
     * initialize the javaFX elements
     */
    private void initJavaFX() {
        searchBar = new ToolBar();
        init_searchBar();

        CloseSearchButton = new Button("Close");
        init_closeSearchButton();

        searchField = new TextField();
        init_searchField();
        match_count = new Label();
        searchBar.getItems().addAll(CloseSearchButton, searchField, match_count);

        listViewArticles.setCellFactory(lv -> new ArticleCell());
        setHelpImages();

    }


    /**
     * Shuts off all windows of active session.
     */
    @FXML
    public void disconnect() {
        for (Stage aStageArrayList : stageArrayList) {
            aStageArrayList.close();
        }
        mainStage.close();
    }

    /**
     * relaunches the application after the disconnecting of windows
     * goes to logging screen
     *
     * @throws Exception exception caused by main
     */
    @FXML
    public void relaunch() throws Exception {
        disconnect();
        Main myMain = new Main();
        myMain.start(new Stage());
    }

    /**
     * Display all the valid _articles in the window
     *
     * @param _articles _articles that haven't been deleted in the DB
     */
    @FXML
    void displayArticles(ArrayList<DatabaseArticle> _articles) {
        listViewArticles.getItems().clear();
        for (DatabaseArticle item : _articles) {
            listViewArticles.getItems().add(item);
        }

    }

    /**
     * @param _path             path of the image
     * @param _height           height of the image
     * @param _width            width of the image
     * @param _readArticleImage place to put the image
     */
    private void setImage(String _path, int _height, int _width, MenuItem _readArticleImage) {
        ImageView readIcon = new ImageView(new Image(_path));
        readIcon.setFitHeight(_height);
        readIcon.setFitWidth(_width);
        _readArticleImage.setGraphic(readIcon);
    }

    void setMainStage(Stage _stage) {
        mainStage = _stage;
    }


    /**
     * load the images of the help menu
     */
    private void setHelpImages() {
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ReadArticle.png", 250, 400, readArticleImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/SearchByTitle.png", 280, 380, searchArticleImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/CopyToClipboard.png", 250, 400, copyArticleLinkImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/Suggestions.png", 350, 550, suggestionImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureSources.png", 380, 530, configureSourcesImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureTags.png", 350, 550, configureTagsImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/Exit.png", 200, 350, exitAppImage);
    }

    @FXML
    private void getArticleAndOpen() throws de.l3s.boilerpipe.BoilerpipeProcessingException {
        DatabaseArticle articleToRead = listViewArticles.getSelectionModel().getSelectedItem();
        openArticleWindow(articleToRead);
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
            MessageBoxes.showErrorBox("Error while copying link to clipboard");
        }
    }

    /**
     * open the window to give suggestion of articles
     */
    @FXML
    private void openSuggestionPopup() {
        TagManager tagManager = new TagManager(dbPath, password);
        ArticleManager articleManager = new ArticleManager(dbPath, password);
        ArrayList<DatabaseArticle> suggestedArticlesList;
        suggestedArticlesList = articleManager.getSuggestion(tagManager.getBest());

        final Stage suggestionWindow = new Stage();
        suggestionWindow.setTitle("Suggestions");
        if (suggestedArticlesList.size() == 0) {
            MessageBoxes.showErrorBox("All suggested articles have been read, you should download more articles with the 'Sources' window");
        } else {
            GridPane gridPane = setSuggestionPanelConstraint(suggestionWindow);

            ArrayList<Button> buttonList = new ArrayList<>();
            int size = (suggestedArticlesList.size() < 3) ? suggestedArticlesList.size() : 3;
            for (int i = 0; i < size; i++) {
                fillSuggestionPanel(suggestedArticlesList.get(i), gridPane, buttonList, i);
            }

            initButtonSuggested(suggestedArticlesList, buttonList);
            showSuggestionScene(gridPane, suggestionWindow);
        }
    }

    /**
     * @param gridPane         gridPane of the window
     * @param suggestionWindow Stage of the window
     */
    private void showSuggestionScene(GridPane gridPane, Stage suggestionWindow) {
        Scene suggestionScene = new Scene(gridPane, 450, 200);
        suggestionWindow.setScene(suggestionScene);
        suggestionWindow.show();
    }

    /**
     * @param _suggestedArticlesList List of the suggested articles
     * @param _buttonList            List of the buttons linked to the article
     */
    private void initButtonSuggested(ArrayList<DatabaseArticle> _suggestedArticlesList, ArrayList<Button> _buttonList) {
        for (int j = 0; j < _buttonList.size(); j++) {
            DatabaseArticle articleToButton = _suggestedArticlesList.get(j);
            _buttonList.get(j).setOnAction(event -> {
                openArticleWindow(articleToButton);
            });
        }
    }

    /**
     * @param _suggestionStage is the Stage that will be modified
     * @return the modified Stage
     */
    private GridPane setSuggestionPanelConstraint(Stage _suggestionStage) {
        _suggestionStage.initModality(Modality.WINDOW_MODAL);
        _suggestionStage.initOwner(mainStage);
        GridPane gridPane = new GridPane();
        ColumnConstraints col1Constraints = new ColumnConstraints();
        col1Constraints.setPercentWidth(30);
        ColumnConstraints col2Constraints = new ColumnConstraints();
        col2Constraints.setPercentWidth(30);
        ColumnConstraints col3Constraints = new ColumnConstraints();
        col3Constraints.setPercentWidth(30);
        gridPane.getColumnConstraints().addAll(col1Constraints, col2Constraints, col3Constraints);
        gridPane.setHgap(20);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    /**
     * @param _suggestedArticle list of suggested articles
     * @param _gridPane         pane to display suggestions
     * @param _buttonList       list of buttons to use to open articles
     * @param _columnIndex      is the index of the column that needs to be modified
     */
    private void fillSuggestionPanel(DatabaseArticle _suggestedArticle, GridPane _gridPane, ArrayList<Button> _buttonList, int _columnIndex) {
        Image icon = new Image(HTMLArticleDownloader.getIconUrl(_suggestedArticle.getLink()), 100, 100, true, true);
        ImageView articleImage = new ImageView(icon);
        _gridPane.add(articleImage, _columnIndex, 0);
        Label articleText = new Label(_suggestedArticle.getTitle());
        articleText.setWrapText(true);
        _gridPane.add(articleText, _columnIndex, 1);
        Button articleReadButton = new Button("Lire cet article");
        _buttonList.add(articleReadButton);
        _gridPane.add(articleReadButton, _columnIndex, 2);
    }


    @FXML
    private void quit() {
        Platform.exit();
    }

    /**
     * @param _loader      _loader
     * @param _titleWindow title of the window
     * @param _title       parameter use for the error message
     */
    private Stage openWindow(FXMLLoader _loader, String _titleWindow, String _title) {
        Stage stage = new Stage();
        try {
            Parent root = _loader.load();
            stage = new Stage();
            stage.setTitle(_titleWindow);
            setStage(root, stage);
            stageArrayList.add(stage);
        } catch (Exception e) {
            MessageBoxes.showErrorBox("Error while opening " + _title + " window!");
        }
        return stage;
    }

    /**
     * Method that opens an article when the user click on it
     */
    @FXML
    private void openArticleWindow(DatabaseArticle _articleToRead) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewSingleArticleController.class.getResource("ViewSingleArticle.fxml"));
            ViewSingleArticleController controller = new ViewSingleArticleController(_articleToRead, dbPath, password);
            loader.setController(controller);
            controller.setArticlesWindows(this);
            Stage articleStage = openWindow(loader, _articleToRead.getTitle(), "article");
            controller.start(articleStage);
        } catch (Exception e) {
            MessageBoxes.showErrorBox("Error while opening the article. Is an article selected?");
        }
    }

    /**
     * Open the window to set user preferences
     */
    public void openUserPreferencesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(UserPreferencesController.class.getResource("UserPreferences.fxml"));
            UserPreferencesController controller = new UserPreferencesController(dbPath, password);
            loader.setController(controller);
            Stage userPreferencesStage = openWindow(loader, "Edit use preferences", "user preferences");
            controller.start(userPreferencesStage);
        } catch (Exception e) {
            MessageBoxes.showErrorBox("Error while opening the tag window!");
        }
    }

    /**
     * Open the window to set sources
     */
    @FXML
    public void openSourceWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(SourceMenuController.class.getResource("SourceMenu.fxml"));
            SourceMenuController controller = new SourceMenuController(dbPath, password);
            loader.setController(controller);
            Stage sourceStage = openWindow(loader, "Manage sources", "sources");
            controller.start(sourceStage);
        } catch (Exception e) {
            MessageBoxes.showErrorBox("Error while opening the Source window!");
        }
    }

    /**
     * @param root  root of the scene
     * @param stage stage of the scene
     */
    private void setStage(Parent root, Stage stage) {
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * If the search bar is available or not. When you close it, it loads all the articles again
     */
    @FXML
    private void changeSearchBarStatus() {
        if (VBox.getChildren().indexOf(searchBar) == -1) {
            VBox.getChildren().add(searchBar);
            match_count.setText("");
        } else {
            VBox.getChildren().remove(searchBar);
            displayArticles(articleManager.loadArticles());
        }
    }

    /**
     * Initialize searchbar parameters
     *  */
    private void init_searchBar() {
        searchBar.setPrefHeight(SEARCHBAR_HEIGHT);
        searchBar.setPrefWidth(SEARCHBAR_WIDTH);
        GridPane.setConstraints(searchBar, 0, 0);
    }

    /**
     * Initialize "close" button from search bar
     */
    private void init_closeSearchButton() {
        CloseSearchButton.setOnAction(event -> changeSearchBarStatus());
    }

    /**
     * Initialize searchField
     */
    private void init_searchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            ArrayList<DatabaseArticle> articles = articleManager.loadArticles(newValue);
            displayArticles(articles);
            match_count.setText(articles.size() + " matches");
        });
    }




}
