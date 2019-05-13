package be.ac.ulb.infof307.g04.view;

import be.ac.ulb.infof307.g04.controller.InternetTester;
import be.ac.ulb.infof307.g04.model.ArticleManager;
import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import be.ac.ulb.infof307.g04.model.SourceManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class ViewSingleArticleController, show what an article looks like when you click on it in the article cell
 *
 * @version 4.0
 * @see DatabaseArticle
 * @see ArticleManager
 */

public class ViewSingleArticleController extends Application {
    //Manager that could allow to delete an article
    private final ArticleManager articleManager;
    private final Timer timer;
    private DatabaseArticle article;
    //Boolean fot the validity of the article
    private boolean isValid;
    private boolean windowActive;
    private Stage primaryStage;

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

    /**
     * Constructor of the view of a single article
     *
     * @param _article article to view
     *                 article that has to be viewed
     */
    public ViewSingleArticleController(DatabaseArticle _article, String _dbPath, String _dbPassword) {
        articleManager = new ArticleManager(_dbPath, _dbPassword);
        article = _article;
        articleManager.openArticle(_article);
        timer = new Timer();
    }

    public void setArticlesWindows(ArticleListController _articlesWindows) {
        articlesWindow = _articlesWindows;
    }


    /**
     * Start javafx window
     */
    @Override
    public void start(Stage _primaryStage) {
        primaryStage = _primaryStage;
        primaryStage.setOnHidden(e -> stop());
        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> windowActive = newValue);
        setFields();
        startTimer();
    }

    @Override
    public void stop() {
        stopTimer();
    }

    /**
     * Initialize the text and the title of the article
     * Modify the integrity circle and text
     */
    public void initialize() throws IOException {
        String htmlFile;
        if (InternetTester.testInternet()) {
            Document doc = Jsoup.connect(article.getLink()).get();
            doc.getElementsByClass("m-privacy-consent").remove();
            htmlFile = doc.toString();
        } else {
            htmlFile = article.getHtmlContent();
        }
        articleView.getEngine().loadContent(htmlFile);
    }

    private void startTimer() {
        TimerTask task = new TimerTask() {
            public void run() {
                if (windowActive) {
                    articleManager.addTimeWatched(article, 1);
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private void stopTimer() {
        timer.cancel();
    }

    /**
     * set integrity and tag files
     */
    private void setFields() {
        primaryStage.setTitle(article.getTitle());
        tagsLabel.setText("Tags: " + article.getTags());
        if (Integer.toString(article.hashCode()).equals(article.getIntegrityHash())) {
            setIntegrityColorText("Untampered article", Color.LIGHTGREEN);
        } else {
            if (InternetTester.testInternet()) {
                if (MessageBoxes.showConfirmationBox("Article is tampered with, do you want to redownload it?")) {
                    try {
                        article = SourceManager.redownloadArticle(article, articleManager.getArticleSource(article));
                        articleManager.upsertArticle(article);
                        setFields();
                    } catch (Exception e) {
                        if (MessageBoxes.showConfirmationBox("Unable to redownload article, would you like to delete it?")) {
                            deleteArticle();
                        }
                    }
                } else {
                    setIntegrityColorText("Article should be redownloaded", Color.ORANGE);
                }
            } else {
                MessageBoxes.showErrorBox("The article has been tampered with. You are currently offline, it cannot be redownloaded :'<");
                setIntegrityColorText("Tampered article", Color.RED);
            }
        }
        updateLikeDislikeButton();
    }

    /**
     * Change the text and color of the integrity label
     *
     * @param s     is the string to set the integrity label to
     * @param color is the color to set the integrity label to
     */
    private void setIntegrityColorText(String s, Color color) {
        integrityLabel.setText(s);
        integrityCircle.setFill(color);
    }


    /**
     * function called when the delete button is pressed
     */
    @FXML
    public void deleteButtonPressed() {
        deleteArticle();
    }

    private void deleteArticle() {
        articleManager.deleteArticle(article);
        articlesWindow.displayArticles(articleManager.loadArticles());
        System.out.println("DatabaseArticle supprimé");
        //close the article page when deleted
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void updateLikeDislikeButton() {
        if (article.getLikeState() == ArticleManager.DISLIKED) {
            dislikeButton.setStyle("-fx-background-color: #ff0000; ");
            likeButton.setStyle("");
        } else if (article.getLikeState() == ArticleManager.LIKED) {
            likeButton.setStyle("-fx-background-color: #0cff00; ");
            dislikeButton.setStyle("");
        } else {
            dislikeButton.setStyle("");
            likeButton.setStyle("");
        }
    }

    @FXML
    public void dislikeButtonPressed() {
        if (article.getLikeState() == ArticleManager.DISLIKED) {
            articleManager.setNeutralLike(article);
        } else {
            articleManager.dislikeArticle(article);
        }
        updateLikeDislikeButton();
    }

    @FXML
    public void likeButtonPressed() {
        if (article.getLikeState() == ArticleManager.LIKED) {
            articleManager.setNeutralLike(article);
        } else {
            articleManager.likeArticle(article);
        }
        updateLikeDislikeButton();
    }
}