package be.ac.ulb.infof307.g04.controller;

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
import java.net.MalformedURLException;
import java.util.Optional;

/**
 * Class ViewSingleArticle, show what an article looks like when you click on it in the article cell
 * @see Article
 * @see ArticleManager
 * @see ArticleVerification
 * @version 4.0
 */

public class ViewSingleArticle extends Application{
    private Article article;

    //Boolean fot the validity of the article
    private boolean is_valid;

    //Manager that could allow to delete an article
    private ArticleManager article_manager = new ArticleManager("./article_db");

    @FXML
    private Label article_title;
    @FXML
    private Label integrity_label;
    @FXML
    private Circle integrity_circle; //integrity of the article represented by a coloured circle
    @FXML
    private Button delete_button;
    @FXML
    private Label tags_label;
    @FXML
    private ImageView article_icon; //image of the article
    @FXML
    private WebView article_view; //whole article

    private Main articles_window; //window that contains the article

    private ArticleVerification verification;


    public ViewSingleArticle(Article _article) throws IOException, ParserConfigurationException, SAXException {
        /**
        Constructor of the view of a single article
         @param _article
                    article that has to be reviewd
         */
        article = _article;
        ArticleVerification verification = new ArticleVerification(article,article.getSource_url());
        check_Integrity(verification.is_valid());

    }

    public void set_articles_windows(Main articles_window_) {
        articles_window = articles_window_;
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        /**
         * Start javafx window
         * @param primaryStage
         */
        //Load an fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ViewSingleArticle.class.getResource("/be/ac/ulb/infof307/g04/view/ViewSingleArticle.fxml"));

        AnchorPane conteneurPrincipal;
        conteneurPrincipal = loader.load();
        Scene scene = new Scene(conteneurPrincipal);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void initialize() throws IOException, ParserConfigurationException, SAXException {
        /**
         * Initialize the text and the title of the article
         * Modify the integrity circle and text
         * @throws Exception : if article wasn't found
         */

        article_view.getEngine().load(article.getLink());

        set_Fields();
    }

    private void set_Fields() throws IOException, ParserConfigurationException, SAXException {
        article_title.setText(article.getTitle());
        handle_Integrity();
        tags_label.setText("Tags: " + article.getTags());
        article_icon.setImage(new Image("/be/ac/ulb/infof307/g04/pictures/Background_Presentation.jpg"));

    }

    private void handle_Integrity() throws IOException, ParserConfigurationException, SAXException {
        //if article is integer -> green ; else -> red
        if (this.is_valid) {
            integrity_label.setText("Article intègre");
            integrity_circle.setFill(Color.web("0x00FF66"));
        } else {
            integrity_label.setText("Non intègre!");
            integrity_circle.setFill(Color.RED);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Erreur intégrité");
            alert.setHeaderText("L'article n'est pas intègre!");
            alert.setContentText("Voulez-vous mettre à jour l'article et les informations le concernant ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                verification.correct_article();
            }
        }
    }

    private void check_Integrity(boolean is_correct) throws IOException, ParserConfigurationException, SAXException {
        /*
        validity of the article
         */
        ArticleVerification verification = new ArticleVerification(article, article.getSource_url());
        this.is_valid = verification.is_valid();
    }


    @FXML
    private void delete_button_pressed(){
        /*
         * function called when the delete button is pressed
         */
        article_manager.delete_article(article);
        articles_window.display_articles(article_manager.load_articles());
        System.out.println("Article supprimé");
        //close the article page when deleted
        Stage stage = (Stage) delete_button.getScene().getWindow();
        stage.close();
    }
}

