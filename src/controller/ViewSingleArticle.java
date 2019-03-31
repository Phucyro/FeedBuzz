package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import be.ac.ulb.infof307.g04.Main;

import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import model.ArticleManager;

public class ViewSingleArticle extends Application{
    private Article article;
    //Booleen qui sera a True ou False en fonction de l'integrite de l'article
    private boolean is_correct;
    //Manager qui permettra de supprimer un article
    private ArticleManager article_manager = new ArticleManager("./article_db");

    @FXML
    //Label contenant le titre de l'article
    private Label article_title;
    @FXML
    //Label contenant le texte d'integrite de l'article
    private Label integrity_label;
    @FXML
    //Cercle colore qui montre l'integrite de l'article
    private Circle integrity_circle;
    @FXML
    //Bouton avec ecrit "Delete"
    private Button delete_button;
    @FXML
    //Label contenant les tags de l'article
    private Label tags_label;
    @FXML
    //Image de l'article
    private ImageView article_icon;
    @FXML
    private WebView article_view;
    private Main articles_window;


    public ViewSingleArticle(Article _article){
       // article_manager = new ArticleManager("./test.db","abcdefgh");
        ParserRss my_parser = new ParserRss();

        article = _article;
        //Enlever tout ca quand le check d'integrite sera mis en place. Appeler la fonction set_integrity avec le  bon booleen et la couleur changera automatiquement
        //article = my_parser.parse("http://rss.cnn.com/rss/cnn_topstories.rss").get(0);
        //System.out.println(article);
        ArticleVerification verification = new ArticleVerification(article,article.getSource_url());
        set_integrity(verification.is_valid());

    }

    public void set_articles_windows(Main articles_window_) {
        articles_window = articles_window_;

    }


    //Fonction a supprimer quand une vraie classe main existe
    public static void main(String[] args) { launch(args); }

    @Override
    //Demarre la fenetre javafx
    public void start(Stage primaryStage) {
        //Loader qui permet de charger le fichier fxml a l'emplacement donne
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ViewSingleArticle.class.getResource("/view/ViewSingleArticle.fxml"));
        try {
            AnchorPane conteneurPrincipal;
            conteneurPrincipal = loader.load();
            Scene scene = new Scene(conteneurPrincipal);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        //Initialise le texte de l'article et son titre, et modifie la couleur du cercle d'integrite ainsi que le texte en fonction de l'integrite de l'article
        try {
            article_view.getEngine().loadContent(article.getDescription());
        } catch (Exception e){
            //System.out.println(article.getDescription());
        }
        article_title.setText(article.getTitle());
        if (this.is_correct){
            integrity_label.setText("Article intègre");
            //Rempli le cercle de couleur verte
            integrity_circle.setFill(Color.web("0x00FF66"));
        }
        else{
            integrity_label.setText("Non intègre!");
            integrity_circle.setFill(Color.RED);
        }
        article_icon.setImage(new Image ("/pictures/Background_Presentation.jpg"));
        //article_icon.setImage(new Image("https://l.facebook.com/l.php?u=https%3A%2F%2Fhelpx.adobe.com%2Fcontent%2Fdam%2Fhelp%2Fen%2Fstock%2Fhow-to%2Fvisual-reverse-image-search%2F_jcr_content%2Fmain-pars%2Fimage%2Fvisual-reverse-image-search-v2_1000x560.jpg%3Ffbclid%3DIwAR33t6CLYiXxRgEMz_ZO9L4oBa7X16W2z9-pC2QyIzKPl9eAJGB2bEAqwRA&h=AT13_ep7eqZ1l6RSGeZ2_nhGIdDy-tfZOcCcCuAPzcyQ6nFrZ-XunWzQEPK-7TndUHhu9Wh7P3HQuVCtEFkA-zoC9vyn8avJt6OoR0kLeZtyvXCv-0ZrJWi-mf9Lng"));
    }

    private boolean get_integrity() {
        return is_correct;
    }

    private void set_integrity(boolean is_correct) {
        ArticleVerification verif = new ArticleVerification(article, article.getSource_url());
        this.is_correct = verif.is_valid();
    }

    @FXML
    private void delete_button_pressed(){
        //Fonction appelee lorsque l'utilisateur appuie sur le bouton "delete"
        article_manager.delete_article(article);
        articles_window.display_articles(article_manager.load_articles());
        System.out.println("Article supprime");
        //Ferme la page de l'article à la supression de celui_ci
        Stage stage = (Stage) delete_button.getScene().getWindow();
        stage.close();
    }
}