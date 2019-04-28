package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;
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
public class ArticleCell extends ListCell<DatabaseArticle> {

    public static final String DEFAULT_ICON = "/be/ac/ulb/infof307/g04/pictures/Background_Presentation.jpg";
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


    /**
     * Function called when an item is modified
     */
    @Override
    protected void updateItem(DatabaseArticle item, boolean _empty){
        /**
         * Function called when an item is modified
         *
         * @throws IOException : if there's no picture found for the cell
         * @throws URISyntaxException : if the link doesn't work
         */
        super.updateItem(item, _empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!_empty && item != null) {
            titleLabel.setText(item.getTitle());
            tagLabel.setText("Tags: "+ item.getTags()); // show tags
            linkLabel.setText(item.getLink());


            popupOverArticle(item);


            Image icon = new Image(DEFAULT_ICON);
            try {
                icon = new Image(getIconUrl(item.getLink(), item.getDescription()));
            } catch (Exception e) {
            }
            articleIcon.setImage(icon);



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

    private void popupOverArticle(DatabaseArticle _item) {
        /**
         * Function called to create a popup that only shows when the mouse is over
         * @param article _item
         */
        String summaryText = "";
        if (_item.getDescription() != null) {
            summaryText = htmlToPlain(_item.getDescription());
        }

        DisplayPreview(gridPane, summaryText);
    }

    private void DisplayPreview(GridPane articlePane, String summary) {
        /**
         * Function called to make the preview of the article
         * @param GridPane _articlePane
         * @param String _summary
         */

        StackPane previewPane = makeSummaryPane();
        Popup popup = makePreviewPopup(summary, previewPane);
        showsWhenMouseOver(articlePane, previewPane, popup);

    }

    private void showsWhenMouseOver(GridPane _articlePane, StackPane _previewPane, Popup _popup) {
        /**
         * Function called to make the popup appear and disappear
         * @param GridPane _articlePane
         * @param GridPane _previewPane
         * @param Popup _popup
         */
        _articlePane.hoverProperty().addListener((obs, oldVal, newValue) -> {

            if (newValue) {
                Bounds bounds = _articlePane.localToScreen(_articlePane.getLayoutBounds());
                double x = bounds.getMinX() - (_previewPane.getWidth() / 2) + (_articlePane.getWidth() / 2);
                double y = bounds.getMinY() - _previewPane.getHeight();
                _popup.show(_articlePane, x, y);
            } else {
                _popup.hide();
            }
        });
    }


    private Popup makePreviewPopup(String _summary, StackPane _previewPane) {
        /**
         * Function called to create the popup
         * @param String _summary
         * @param StackPane _previewPane
         * @return Popup
         */
        TextArea resume = new TextArea(_summary);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        //Text area options
        resume.setWrapText(true);
        resume.setEditable(false);

        //Layout options
        vBox.getChildren().addAll(resume);
        _previewPane.getChildren().addAll(vBox);

        Popup popup = new Popup();
        popup.getContent().add(_previewPane);
        return popup;
    }


    private StackPane makeSummaryPane() {
        /**
         * Function called to create the previewPane
         * @return Stackpane
         */

        StackPane previewPane = new StackPane();
        previewPane.setPrefSize(200, 200);
        previewPane.setStyle("-fx-font-style: italic");
        return previewPane;
    }



    /**
     * Retrieve first icon url in html text
     *
     * @param _descriptionHtml html file to parse
     * @return url to an image
     */
    private String getIconUrl(String _articleLink, String _descriptionHtml) throws IOException {
        return HTMLArticleDownloader.getIconFromDescription(_articleLink, _descriptionHtml);
    }

    /**
     * get the plain text of a html string
     * @param html html file
     */
    private String htmlToPlain(String html)
    {
        return Jsoup.parse(html).text();
    }
}