package be.ac.ulb.infof307.g04.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Define an article cell on the main window
 */
public class ArticleCell extends ListCell<Article> {

    private final GridPane gridPane = new GridPane();
    private final ImageView articleIcon = new ImageView();
    private final Label titleLabel = new Label();
    private final Label tagLabel = new Label();
    private final Hyperlink linkLabel = new Hyperlink();
    private final AnchorPane content = new AnchorPane();


    public ArticleCell() {
        /**
         * Constructor of the article cell
         *
         * @version 3.0
         * @see GridPane
         * @see ImageView
         * @see Label
         * @see Hyperlink
         * @see AnchorPane
         */
        articleIcon.setFitWidth(75);
        articleIcon.setPreserveRatio(true);
        GridPane.setConstraints(articleIcon, 0, 0, 1, 2);
        GridPane.setValignment(articleIcon, VPos.TOP);
        //
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1em;");
        GridPane.setConstraints(titleLabel, 1, 0);
        tagLabel.setStyle("-fx-font-size: 0.9em;");
        GridPane.setConstraints(tagLabel, 2, 0);
        //
        //descriptionWebView.setMaxSize(400, 100);
        //GridPane.setConstraints(descriptionWebView, 2, 0, 1, 1);
        //
        GridPane.setConstraints(linkLabel, 1, 1, 2, 1);

        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.getChildren().setAll(articleIcon, titleLabel, tagLabel, linkLabel);

        AnchorPane.setTopAnchor(gridPane, 0d);
        AnchorPane.setLeftAnchor(gridPane, 0d);
        AnchorPane.setBottomAnchor(gridPane, 0d);
        AnchorPane.setRightAnchor(gridPane, 0d);
        content.getChildren().add(gridPane);
    }

    @Override
    protected void updateItem(Article item, boolean empty) {
        /**
         * Function called when an item is modified
         *
         * @throws IOException : if there's no picture found for the cell
         * @throws URISyntaxException : if the link doesn't work
         */
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!empty && item != null) {
            titleLabel.setText(item.getTitle());
            tagLabel.setText("Tags: "+ item.getTags()); // show tags
            //descriptionWebView.getEngine().loadContent(item.getDescription());
            //articleIcon.setImage(item.());
            linkLabel.setText(item.getLink());

            //Setup the preview popup over the article
            String summaryText = "";
            if (item.getDescription() != null) {
                summaryText = htmlToPlain(item.getDescription());
            }

            //show a preview of the article description
            PreviewDisplay.mouseOverArticle(gridPane, summaryText);

            //Show the image icon
            String imageUrl = null;

            try {
                imageUrl = null;
                if (item.getDescription() != null) {
                    imageUrl = getIconUrl(item.getDescription());
                }

                if (imageUrl != null)
                {
                    Image icon = new Image(imageUrl);
                    articleIcon.setImage(icon);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            linkLabel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        java.awt.Desktop.getDesktop().browse(new URI(item.getLink()));
                    } catch (URISyntaxException | IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            
            setText(null);
            setGraphic(content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private String getIconUrl(String texte) throws IOException {

        /**
         * Retrieve first icon url in html text
         *
         * @param texte
         *          html file to parse
         * @return url to an image
         * @throws IOException : if there's no url to the icon
         */

        String imageUrl = null;
        Document doc = Jsoup.parse(texte);
        Elements imgs = doc.getElementsByTag("img");

        boolean found = false;

        for (Element elem: imgs)
        {
            if (!found)
            {
                imageUrl = elem.absUrl("src");

                found = true;
            }
        }

        return imageUrl;
    }

    /**
     * get the plain text of a html string
     * @param html
     *          html file
     * @return the text of an html string
     *
     */
    private String htmlToPlain(String html)
    {
        return Jsoup.parse(html).text();
    }
}