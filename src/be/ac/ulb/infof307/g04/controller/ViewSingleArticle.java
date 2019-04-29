package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.Main;
import be.ac.ulb.infof307.g04.model.ArticleManager;
import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

/**
 * Class ViewSingleArticle, show what an article looks like when you click on it in the article cell
 * @see DatabaseArticle
 * @see ArticleManager
 * @see ArticleVerification
 * @version 4.0
 */

public class ViewSingleArticle extends Application{
    private DatabaseArticle article;

    //Boolean fot the validity of the article
    private boolean isValid;

    //Manager that could allow to delete an article
    private ArticleManager articleManager = new ArticleManager("./article_db");

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

    private Main articlesWindow; //window that contains the article

    private ArticleVerification verification;


    /**
      *Constructor of the view of a single article
      *@param _article article to view
      *article that has to be reviewd
      */
    public ViewSingleArticle(DatabaseArticle _article) throws IOException, ParserConfigurationException, SAXException, ParseException {

        article = _article;
        System.out.println(article);
        if (InternetTester.testInternet()) {
            ArticleVerification verification = new ArticleVerification(article, article.getSourceUrl());
            checkIntegrity();
        }
        //ArticleVerification verification = new ArticleVerification(article,article.getSource_url());
        //set_integrity(verification.is_valid());

        //TODO article verification (propre)
    }

    public void setArticlesWindows(Main _articlesWindows) {
        articlesWindow = _articlesWindows;
    }


    /**
     * Start javafx window
     */
    @Override
    public void start(Stage _primaryStage) {}

    /**
     * Initialize the text and the title of the article
     * Modify the integrity circle and text
     * @throws IOException : if article wasn't found
     */
    public void initialize() throws IOException, ParserConfigurationException, SAXException, ParseException {
        System.out.println(article.getHtmlContent());
        if(InternetTester.testInternet()) {
            articleView.getEngine().load(article.getLink());
        }
        else {
            articleView.getEngine().loadContent(article.getHtmlContent());
        }
        setFields();
    }

    /**
     * set integrity and tag files
     */
    private void setFields() throws IOException, ParserConfigurationException, SAXException, ParseException {
        handleIntegrity();
        tagsLabel.setText("Tags: " + article.getTags());

    }

    private void handleIntegrity() throws IOException, ParserConfigurationException, SAXException, ParseException {
        //if article is integer -> green ; else -> red
        if (InternetTester.testInternet()){
            if (this.isValid) {
                integrityLabel.setText("DatabaseArticle intègre");
                integrityCircle.setFill(Color.web("0x00FF66"));
            } else {
                integrityLabel.setText("Non intègre!");
                integrityCircle.setFill(Color.RED);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Erreur intégrité");
                alert.setHeaderText("L'article n'est pas intègre!");
                alert.setContentText("Voulez-vous mettre à jour l'article et les informations le concernant ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    verification.correctArticle();
                }
            }
        }
        else{
            integrityLabel.setText("Pas d'iternet");
            integrityCircle.setFill(Color.ORANGE);
        }
    }

    /**
     * validity of the article
     */
    private void checkIntegrity() throws IOException, ParserConfigurationException, SAXException, ParseException {
        if (InternetTester.testInternet()) {
            ArticleVerification verification = new ArticleVerification(article, article.getSourceUrl());
            this.isValid = verification.isValid();
        }
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
}