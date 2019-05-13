package be.ac.ulb.infof307.g04.view;


import be.ac.ulb.infof307.g04.Main;
import be.ac.ulb.infof307.g04.controller.ArticleCell;
import be.ac.ulb.infof307.g04.controller.HTMLArticleDownloader;
import be.ac.ulb.infof307.g04.controller.InternetTester;
import be.ac.ulb.infof307.g04.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.xml.sax.SAXException;

import javax.swing.text.html.HTML;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    private static ArticleManager articleManager;
    private static SourceManager source;

    private ToolBar searchBar;
    private Button CloseSearchButton;
    private TextField searchField;
    private Label match_count;
    private final String dbPath;
    private final String password;
    private final ArrayList <Stage> stageArrayList = new ArrayList<>();
    private Stage mainStage;
    private static final String DEFAULT_ICON = "/be/ac/ulb/infof307/g04/pictures/Background_Presentation.jpg";


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
        init_db();
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
            }
        }else{
            MessageBoxes.showErrorBox("Pas d'internet");
        }
    }

    /**
     * initialize the javaFX elements
     */
    private void initJavaFX() {
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
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/Suggestions.png",350,550,suggestionImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureSources.png", 380, 530, configureSourcesImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/ConfigureTags.png", 350, 550, configureTagsImage);
        setImage("/be/ac/ulb/infof307/g04/pictures/Help_Pictures/Exit.png", 200, 350, exitAppImage);
    }

    @FXML
    private void getArticleAndOpen() throws de.l3s.boilerpipe.BoilerpipeProcessingException{
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
        if (suggestedArticlesList.size() == 0){
            MessageBoxes.showErrorBox("All suggested articles have been read, you should download more articles with the 'Sources' window");
        }
        else {
            GridPane gridPane = setSuggestionPanelConstraint(suggestionWindow);
            ArrayList<Button> buttonList = new ArrayList<>();
            int size = (suggestedArticlesList.size() < 3) ? suggestedArticlesList.size() : 3;
            for (int i = 0; i < size; i++) {
                fillSuggestionPanel(suggestedArticlesList.get(i), gridPane, buttonList, i);
            }

            initButtonSuggested(suggestedArticlesList, buttonList);

            setPopup(suggestionWindow, gridPane);
        }
    }

    private void setPopup(Stage suggestionWindow, GridPane gridPane) {
        Scene suggestionScene = new Scene(gridPane, 450, 200);
        suggestionWindow.setScene(suggestionScene);
        suggestionWindow.show();
    }

    /**
     * @param _suggestedArticlesList List of the suggested articles
     * @param _buttonList List of the buttons linked to the article
     */
    private void initButtonSuggested(ArrayList<DatabaseArticle> _suggestedArticlesList, ArrayList<Button> _buttonList) {
        for (int j = 0; j < _buttonList.size(); j++) {
            DatabaseArticle articleToButton = _suggestedArticlesList.get(j);
            System.out.println(_suggestedArticlesList.get(j).getTitle());
            _buttonList.get(j).setOnAction(event -> {
                try{
                    openArticleWindow(articleToButton);
                } catch (de.l3s.boilerpipe.BoilerpipeProcessingException e) {
                    //TODO gestion de l'erreur/ne pas devoir le faire parce que le traitement du texte ne doit pas se faire a l'ouverture de l'article
                }
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
        setColumnsConstraints(gridPane);
        setGridPaneLayout(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private void setGridPaneLayout(GridPane gridPane) {
        gridPane.setHgap(20);
        gridPane.setVgap(5);
    }

    private void setColumnsConstraints(GridPane gridPane) {
        ColumnConstraints col1Constraints = new ColumnConstraints();
        col1Constraints.setPercentWidth(30);
        ColumnConstraints col2Constraints = new ColumnConstraints();
        col2Constraints.setPercentWidth(30);
        ColumnConstraints col3Constraints = new ColumnConstraints();
        col3Constraints.setPercentWidth(30);
        gridPane.getColumnConstraints().addAll(col1Constraints, col2Constraints, col3Constraints);
    }

    /**
     * @param _suggestedArticle list of suggested articles
     * @param _gridPane pane to display suggestions
     * @param _buttonList list of buttons to use to open articles
     * @param _columnIndex is the index of the column that needs to be modified
     */
    private void fillSuggestionPanel(DatabaseArticle _suggestedArticle, GridPane _gridPane, ArrayList<Button> _buttonList, int _columnIndex) {

        String iconUrl = setSuggestionIconUrl(_suggestedArticle);
        ImageView articleImage = getImageView(iconUrl);

        Label articleText = new Label(_suggestedArticle.getTitle());

        Button articleReadButton = getButton(_buttonList);

        articleText.setWrapText(true);

        _gridPane.add(articleText,_columnIndex,1);
        _gridPane.add(articleImage,_columnIndex,0);
        _gridPane.add(articleReadButton,_columnIndex,2);
    }

    private Button getButton(ArrayList<Button> _buttonList) {
        Button articleReadButton = new Button("Lire cet article");
        _buttonList.add(articleReadButton);
        return articleReadButton;
    }

    private ImageView getImageView(String iconUrl) {
        Image icon = new Image(iconUrl, 100, 100, true, true);
        return new ImageView(icon);
    }

    /**
     * @param _suggestedArticle article that is suggested to display
     * @return string that contains the uri of the article's icon
     */
    private String setSuggestionIconUrl(DatabaseArticle _suggestedArticle) {
        String iconUrl = "";
        try {
            iconUrl = HTMLArticleDownloader.getIconUrlFromArticleUrl(_suggestedArticle.getLink());
            iconUrl = "file://" + iconUrl;
        } catch(FileNotFoundException e) {
            iconUrl = DEFAULT_ICON;
        }
        return iconUrl;
    }

    @FXML
    private void quit(){
        Platform.exit();
    }

    /**
     * @param _loader _loader
     * @param _title_window title of the window
     * @param _title parameter use for the error message
     */
    public Stage openWindow(FXMLLoader _loader, String _title_window, String _title){
        Stage stage = new Stage();
        try {
            Parent root = _loader.load();
            stage = new Stage();
            stage.setTitle(_title_window);
            setStage(root, stage);
            stageArrayList.add(stage);
        }
        catch (Exception e) {
            MessageBoxes.showErrorBox("Error while opening "+ _title + " window!");
        }
        return stage;
    }

    /**
     * Method that opens an article when the user click on it
     */
    @FXML
    private void openArticleWindow(DatabaseArticle _articleToRead) throws de.l3s.boilerpipe.BoilerpipeProcessingException {
        try {
            FXMLLoader loader = new FXMLLoader(ViewSingleArticleController.class.getResource("ViewSingleArticle.fxml"));
            ViewSingleArticleController controller = new ViewSingleArticleController(_articleToRead, dbPath, password);
            loader.setController(controller);
            controller.setArticlesWindows(this);
            Stage articleStage = openWindow(loader, _articleToRead.getTitle(), "article");
            controller.start(articleStage);
        } catch (Exception e){
            MessageBoxes.showErrorBox("No article selected");
        }
    }
    /**
     * open the tag window
     */
    public void openTagWindow(ActionEvent _actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(TagMenuController.class.getResource("TagMenu.fxml"));
            TagMenuController controller = new TagMenuController(dbPath, password);
            loader.setController(controller);
            Stage tagStage = openWindow(loader, "Manage tags", "tag");
            controller.start(tagStage);
        } catch (Exception e) {
            MessageBoxes.showErrorBox("Error while opening the tag window!");
        }
    }

    /**
     * @param _actionEvent opens the SourceWindow (download settings)
     */
    @FXML
    public void openSourceWindow(ActionEvent _actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(SourceMenuController.class.getResource("SourceMenu.fxml"));
            SourceMenuController controller = new SourceMenuController(dbPath, password);
            loader.setController(controller);
            Stage sourceStage = openWindow(loader,"Manage sources","sources");
            controller.start(sourceStage);
        } catch (Exception e) {
            MessageBoxes.showErrorBox("Error while opening the Source window!");
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
     * Initialize searchbar parameters
     * @param _height
     * @param _width
     */
    private void init_searchBar(int _height, int _width){

        searchBar.setPrefHeight(_height);
        searchBar.setPrefWidth(_width);
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


    /**
     * Initialize all the tags
     */
    private void init_tags() {
        TagManager tagManager = new TagManager(dbPath, password);
        JSONParser parser = new JSONParser();
        try {
            Object objectParser = parser.parse(new FileReader("src/be/ac/ulb/infof307/g04/model/wordlists.json")); // parse the json, each entry has the label as the key and an array of words as value
            JSONObject object = (JSONObject) objectParser;

            DatabaseTag tag;
            // iterate through the keys of the JSONObject
            for (Object o : object.keySet()) {
                setTag(tagManager, (String) o);
            }
        } catch (org.json.simple.parser.ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set Tag
     * @param tagManager
     * @param lab label
     */
    private void setTag(TagManager tagManager, String lab) {
        DatabaseTag tag;
        tag = new DatabaseTag();
        String label = lab;
        tag.setName(label);
        tagManager.addTag(tag);
    }

    /**
     * Initialize all the sources
     */
    private void init_sources() {
        SourceManager sourceManager = new SourceManager(dbPath, password);
        ArrayList<DatabaseSource> sources = new ArrayList<>();
        sources.add(new DatabaseSource("The Verge", "https://www.theverge.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("BBC world news", "http://feeds.bbci.co.uk/news/world/rss.xml"));
        sources.add(new DatabaseSource("Polygon", "https://www.polygon.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("Vox", "https://www.vox.com/rss/world/index.xml"));
        sources.add(new DatabaseSource("CNN Money", "http://rss.cnn.com/rss/money_topstories.rss","Business"));
        sources.forEach(sourceManager::addSource);
    }



};
