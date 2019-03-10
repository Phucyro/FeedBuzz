package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

public class ViewSingleArticle extends Application{
    private Article article;
    //Booleen qui sera a True ou False en fonction de l'integrite de l'article
    private boolean is_correct;
    @FXML
    //Label contenant le texte de l'article
    private Label article_label;
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


    public ViewSingleArticle(){
        ParserRss my_parser = new ParserRss();

        //Enlever tout ca quand le check d'integrite sera mis en place. Appeler la fonction set_integrity avec le  bon booleen et la couleur changera automatiquement
        article = my_parser.parse("http://rss.cnn.com/rss/cnn_topstories.rss").get(0);
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        System.out.println(article);
        set_integrity(true);

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
        article_label.setText(article.getDescription());
        article_title.setText(article.getTitle());
        if (this.is_correct){
            integrity_label.setText("Cet article est intègre");
            //Rempli le cercle de couleur verte
            integrity_circle.setFill(Color.web("0x00FF66"));
        }
        else{
            integrity_label.setText("Cet article n'est pas intègre!");
            integrity_circle.setFill(Color.RED);
        }
    }

    private boolean get_integrity() {
        return is_correct;
    }

    private void set_integrity(boolean is_correct) {
        this.is_correct = is_correct;
    }
}