package be.ac.ulb.infof307.g04.controller;


import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import be.ac.ulb.infof307.g04.Main;
import be.ac.ulb.infof307.g04.model.ArticleManager;
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
    private Label articleTitle;
    @FXML
    private Label integrityLabel;
    @FXML
    private Circle integrityCircle; //integrity of the article represented by a coloured circle
    @FXML
    private Button deleteButton;
    @FXML
    private Label tagsLabel;
    @FXML
    private ImageView articleIcon; //image of the article
    @FXML
    private WebView articleView; //whole article

    private Main articlesWindow; //window that contains the article

    private ArticleVerification verification;


    public ViewSingleArticle(DatabaseArticle _article) throws IOException, ParserConfigurationException, SAXException, ParseException {
        /**
        Constructor of the view of a single article
         @param _article
                    article that has to be reviewd
         */
        article = _article;
        ArticleVerification verification = new ArticleVerification(article,article.getSourceUrl());
        checkIntegrity(verification.isValid());

    }

    public void setArticlesWindows(Main _articlesWindows) {
        articlesWindow = _articlesWindows;
    }


    @Override
    public void start(Stage _primaryStage) throws IOException {
        /**
         * Start javafx window
         * @param _primaryStage
         */
        //Load an fxml file
        /**
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ViewSingleArticle.class.getResource("/be/ac/ulb/infof307/g04/view/ViewSingleArticle.fxml"));

        AnchorPane conteneurPrincipal;
        conteneurPrincipal = loader.load();
        Scene scene = new Scene(conteneurPrincipal);
        _primaryStage.setScene(scene);
        _primaryStage.show();
        **/
    }

    public void initialize() throws IOException, ParserConfigurationException, SAXException, ParseException {
        /**
         * Initialize the text and the title of the article
         * Modify the integrity circle and text
         * @throws Exception : if article wasn't found
         */

        articleView.getEngine().load(article.getLink());

        setFields();
    }

    private void setFields() throws IOException, ParserConfigurationException, SAXException, ParseException {
        articleTitle.setText(article.getTitle());
        handleIntegrity();
        tagsLabel.setText("Tags: " + article.getTags());
        articleIcon.setImage(new Image("/be/ac/ulb/infof307/g04/pictures/Background_Presentation.jpg"));

    }

    private void handleIntegrity() throws IOException, ParserConfigurationException, SAXException, ParseException {
        //if article is integer -> green ; else -> red
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

    private void checkIntegrity(boolean _isCorrect) throws IOException, ParserConfigurationException, SAXException, ParseException {
        /*
        validity of the article
         */
        ArticleVerification verification = new ArticleVerification(article, article.getSourceUrl());
        this.isValid = verification.isValid();
    }


    @FXML
    private void deleteButtonPressed(){
        /*
         * function called when the delete button is pressed
         */
        articleManager.deleteArticle(article);
        articlesWindow.displayArticles(articleManager.loadArticles());
        System.out.println("DatabaseArticle supprimé");
        //close the article page when deleted
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }
}

