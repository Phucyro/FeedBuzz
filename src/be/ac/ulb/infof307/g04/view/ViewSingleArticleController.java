package be.ac.ulb.infof307.g04.view;

import be.ac.ulb.infof307.g04.controller.ArticleVerification;
import be.ac.ulb.infof307.g04.controller.InternetTester;
import be.ac.ulb.infof307.g04.model.ArticleLabelizer;
import be.ac.ulb.infof307.g04.model.ArticleManager;
import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class ViewSingleArticleController, show what an article looks like when you click on it in the article cell
 * @see DatabaseArticle
 * @see ArticleManager
 * @see ArticleVerification
 * @version 4.0
 */

public class ViewSingleArticleController extends Application{
    private final DatabaseArticle article;

    //Boolean fot the validity of the article
    private boolean isValid;

    //Manager that could allow to delete an article
    private final ArticleManager articleManager;
    private final Timer timer;
    private boolean windowActive;

    @FXML
    private Label integrityLabel;
    @FXML
    private Circle integrityCircle; //integrity of the article represented by a coloured circle
    @FXML
    private Button deleteButton;
    @FXML
    private Label tagsLabel;
    @FXML
    private WebView articleView; //whole article
    @FXML
    private Button likeButton;
    @FXML
    private Button dislikeButton;

    private ArticleListController articlesWindow; //window that contains the article

    private ArticleVerification verification;


    /**
      *Constructor of the view of a single article
      *@param _article article to view
      *article that has to be viewed
      */
    public ViewSingleArticleController(DatabaseArticle _article, String _dbPath, String _dbPassword) throws de.l3s.boilerpipe.BoilerpipeProcessingException{
        articleManager = new ArticleManager(_dbPath, _dbPassword);
        article = _article;
        articleManager.openArticle(_article);
        //ArticleLabelizer mk = new ArticleLabelizer(article);
        //mk.labelize();
        timer = new Timer();
        if (InternetTester.testInternet()) {
            //ArticleVerification verification = new ArticleVerification(article, article.getSourceUrl());
            //checkIntegrity(); Not supported yet
        }
    }

    public void setArticlesWindows(ArticleListController _articlesWindows) {
        articlesWindow = _articlesWindows;
    }


    /**
     * Start javafx window
     */
    @Override
    public void start(Stage _primaryStage) {
        _primaryStage.setOnHidden(e -> stop());
        _primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> windowActive = newValue);
    }

    @Override
    public void stop(){
        stopTimer();
    }

    /**
     * Initialize the text and the title of the article
     * Modify the integrity circle and text
     */
    public void initialize() throws IOException {
        String htmlFile;
        if(InternetTester.testInternet()) {
            Document doc = Jsoup.connect(article.getLink()).get();
            doc.getElementsByClass("m-privacy-consent").remove();
            htmlFile = doc.toString();
        }
        else {
            htmlFile = article.getHtmlContent();
        }
        articleView.getEngine().loadContent(htmlFile);

        setFields();
        startTimer();
    }

    private void startTimer() {
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                if (windowActive) {
                    articleManager.addTimeWatched(article, 1);
                }
            }
        };
        timer.schedule(task,0,1000);
    }

    private void stopTimer() {
        timer.cancel();
    }

    /**
     * set integrity and tag files
     */
    private void setFields() {
        //handleIntegrity(); Not supported yet
        tagsLabel.setText("Tags: " + article.getTags());
        if (InternetTester.testInternet()){
            integrityLabel.setText("Connected to internet");
            integrityCircle.setFill(Color.GREEN);
        } else {
            integrityLabel.setText("No connexion");
            integrityCircle.setFill(Color.ORANGE);
        }
        updateLikeDislikeButton();

    }


    /**
     * function called when the delete button is pressed
     */
    @FXML
    public void deleteButtonPressed(){
        articleManager.deleteArticle(article);
        articlesWindow.displayArticles(articleManager.loadArticles());
        System.out.println("DatabaseArticle supprimé");
        //close the article page when deleted
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void updateLikeDislikeButton(){
        if (article.getLikeState() == ArticleManager.DISLIKED) {
            dislikeButton.setStyle("-fx-background-color: #ff0000; ");
            likeButton.setStyle("");
        } else if (article.getLikeState() == articleManager.LIKED) {
            likeButton.setStyle("-fx-background-color: #0cff00; ");
            dislikeButton.setStyle("");
        } else {
            dislikeButton.setStyle("");
            likeButton.setStyle("");
        }
    }

    @FXML
    public void dislikeButtonPressed(){
        if (article.getLikeState() == ArticleManager.DISLIKED) {
            articleManager.setNeutralLike(article);
        } else {
            articleManager.dislikeArticle(article);
        }
        updateLikeDislikeButton();
    }

    @FXML
    public void likeButtonPressed(){
        if (article.getLikeState() == ArticleManager.LIKED) {
            articleManager.setNeutralLike(article);
        } else {
            articleManager.likeArticle(article);
        }
        updateLikeDislikeButton();
    }
}