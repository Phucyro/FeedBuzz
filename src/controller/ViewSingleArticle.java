package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Date;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ViewSingleArticle extends Application{
    private controller.Article article;
    @FXML
    private Label article_label;
    @FXML
    private Label article_title;


    public ViewSingleArticle(){
        Date date1 = new Date(2001,9,10);
        Date date2 = new Date(2002,9,10);
        String title = "Ceci est le titre de l'article";
        String description = "Ceci est un long article\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus vel ipsum vitae nisl efficitur lacinia aliquet id felis. Mauris vestibulum dolor sem, sed aliquet ex eleifend quis. Aliquam dolor enim, luctus non tempus non, blandit vitae nulla. Ut dictum at mauris quis convallis. Suspendisse potenti. Vestibulum lacinia consectetur tellus. Morbi egestas faucibus erat, in tristique enim sodales id. Phasellus dignissim turpis non tellus consectetur accumsan.Nullam metus neque, commodo sit amet porta ut, scelerisque at leo. Pellentesque sit amet nisi lectus. Maecenas consectetur felis eu arcu eleifend, eu lobortis magna faucibus. Vestibulum tempus felis ipsum, ac ullamcorper sem hendrerit in. Sed nibh leo, condimentum non tincidunt eu, tincidunt vitae tellus. Pellentesque lacus eros, laoreet vitae nunc ac, egestas maximus ipsum. Quisque ac scelerisque magna, eu accumsan nunc. In vel urna vestibulum, porttitor odio eget, blandit elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas non aliquet lectus. Suspendisse eget venenatis dui, nec fermentum mauris. Nunc orci urna, ornare eu dolor scelerisque, finibus facilisis sem. Nunc erat nulla, hendrerit sed posuere sit amet, consectetur ac dolor. In volutpat eros arcu, quis sagittis dolor dapibus eu.Vestibulum tellus nisl, porta a quam sit amet, ullamcorper gravida tortor. In hac habitasse platea dictumst. Etiam maximus feugiat felis. Vestibulum metus nunc, mollis a ornare ut, tincidunt eu urna. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In congue aliquam erat, quis tincidunt urna placerat ac. Fusce justo lectus, congue in laoreet at, venenatis quis sem. Etiam egestas suscipit elit sit amet consectetur. Pellentesque venenatis vestibulum imperdiet. Suspendisse eu aliquet nulla. Donec vehicula, turpis dictum vestibulum tincidunt, purus ante tristique lorem, vitae ullamcorper lorem odio id nulla. Sed est odio, tincidunt in lorem lacinia, molestie dapibus mauris. Curabitur a urna vel orci auctor maximus eu consectetur nunc. Phasellus enim erat, convallis id odio eu, luctus lacinia diam. Maecenas et mauris orci.Sed interdum eget purus non convallis. Suspendisse ac tellus quam. Nam eget orci fringilla, volutpat nulla ac, consequat libero. Vestibulum placerat laoreet lacinia. Nullam bibendum blandit tellus aliquam viverra. Nunc sed laoreet massa. Fusce vel vulputate neque, at accumsan elit. Aliquam congue velit a diam accumsan scelerisque. Maecenas feugiat odio vitae dignissim accumsan. Donec aliquam, nunc at pellentesque gravida, est dui tempus erat, eu blandit ante turpis nec neque. Duis sed elit porta, lacinia mi viverra, mollis elit.Nulla eu porta ligula. Sed et lobortis nisl, ut gravida libero. Proin et orci ligula. Quisque pellentesque feugiat pharetra. Curabitur sed neque elit. Etiam lorem mi, varius pellentesque mi a, rhoncus porta sapien. Fusce lectus diam, tincidunt ac massa vel, rhoncus vestibulum est. Morbi a ex mauris. Duis pharetra dui nec risus mattis placerat. Fusce sit amet lectus non diam imperdiet efficitur.";
        String link = "www.a.com";
        String author = "John Doe";
        article = new Article(date1,date2,title,description,link,author);
    }
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
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
        article_label.setText(article.get_description());
        article_title.setText(article.get_title());
    }
}