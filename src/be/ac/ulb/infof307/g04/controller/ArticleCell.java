package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseArticle;
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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Define an article cell on the main window
 */
public class ArticleCell extends ListCell<DatabaseArticle> {

    private static final int GAP_LENGTH = 6;
    private final GridPane gridPane = new GridPane();
    private final ImageView articleIcon = new ImageView();
    private final Label titleLabel = new Label();
    private final Label tagLabel = new Label();
    private final Hyperlink linkLabel = new Hyperlink();
    private final AnchorPane content = new AnchorPane();

    /**
     * Constructor of the article cell
     *
     * @see GridPane
     * @see ImageView
     * @see Label
     * @see Hyperlink
     * @see AnchorPane
     */
    public ArticleCell() {
        initIcon();
        initLabels();
        setAnchorPane();
        content.getChildren().add(gridPane);
    }

    /**
     * Set the display of the icon of the article
     */
    private void initIcon() {
        articleIcon.setFitWidth(75);
        articleIcon.setPreserveRatio(true);
        GridPane.setConstraints(articleIcon, 0, 0, 1, 2);
        GridPane.setValignment(articleIcon, VPos.TOP);
    }

    /**
     * Set the display for the title, the icon, tags and the link of the article
     */
    private void initLabels() {
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1em;");
        GridPane.setConstraints(titleLabel, 1, 0);
        tagLabel.setStyle("-fx-font-size: 0.9em;");
        GridPane.setConstraints(tagLabel, 2, 0);
        GridPane.setConstraints(linkLabel, 1, 1, 2, 1);

        setGridPane();
        gridPane.getChildren().setAll(articleIcon, titleLabel, tagLabel, linkLabel);
    }

    /**
     * Function called when an item is modified
     *
     * @param item   represents an article
     * @param _empty verify if there's something to update or not
     */
    @Override
    protected void updateItem(DatabaseArticle item, boolean _empty) {
        super.updateItem(item, _empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!_empty && item != null) {
            setLabels(item);
            popupOverArticle(item);
            setImage(item);
            initActionLabel(item);
            setText(null);
            setGraphic(content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    /**
     * Set the title, tags and link for an article
     *
     * @param _item represents an article in the database
     */
    private void setLabels(DatabaseArticle _item) {
        titleLabel.setText(_item.getTitle());
        tagLabel.setText("Tags: " + _item.getTags()); // show tags
        linkLabel.setText(_item.getLink());
    }

    /**
     * Set the icon
     *
     * @param _item represents an article in the database
     */
    private void setImage(DatabaseArticle _item) {
        Image icon;
        icon = new Image(HTMLArticleDownloader.getIconUrl(_item.getLink()));
        articleIcon.setImage(icon);
    }

    /**
     * Allows us to click on the article that we want
     *
     * @param _item represents an article
     */
    private void initActionLabel(DatabaseArticle _item) {
        linkLabel.setOnAction(e -> {
            if (java.awt.Desktop.isDesktopSupported()) {
                new Thread(() -> {
                    try {
                        java.awt.Desktop.getDesktop().browse(new URI(_item.getLink()));
                    } catch (IOException | URISyntaxException exception) {
                        throw new UncheckedIOException((IOException) exception);
                    }
                }).start();
            }
        });
    }

    /**
     * Function called to create a popup that only shows when the mouse is over
     *
     * @param _item represents an article
     */
    private void popupOverArticle(DatabaseArticle _item) {
        String summaryText = "";
        if (_item.getDescription() != null) {
            summaryText = htmlToPlain(_item.getDescription());
        }

        displayPreview(gridPane, summaryText);
    }

    /**
     * Function called to make the preview of the article
     *
     * @param _articlePane article pane where show the preview
     * @param _summary     description of the article
     */
    private void displayPreview(GridPane _articlePane, String _summary) {
        StackPane previewPane = makeSummaryPane();
        Popup popup = makePreviewPopup(_summary, previewPane);
        showsWhenMouseOver(_articlePane, previewPane, popup);
    }

    /**
     * Function called to make the popup appear and disappear
     *
     * @param _articlePane article pane where show the preview
     * @param _previewPane preview pane where show the preview
     * @param _popup       popup where to show the preview
     */
    private void showsWhenMouseOver(GridPane _articlePane, StackPane _previewPane, Popup _popup) {

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

    /**
     * Function called to create the popup
     *
     * @param _summary     summary to print in the popup
     * @param _previewPane preview pane where the summary is shown
     * @return Popup
     */
    private Popup makePreviewPopup(String _summary, StackPane _previewPane) {
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

    /**
     * Function called to create the previewPane
     *
     * @return Stackpane
     */
    private StackPane makeSummaryPane() {
        StackPane previewPane = new StackPane();
        previewPane.setPrefSize(200, 200);
        previewPane.setStyle("-fx-font-style: italic");
        return previewPane;
    }

    /**
     * get the plain text of a html string
     *
     * @param html html file
     */
    private String htmlToPlain(String html) {
        return Jsoup.parse(html).text();
    }

    /**
     * Initialize Gridpane
     */
    private void setGridPane() {
        setGridPaneColumnConstraints();
        setGridPaneColumnConstraints();
        setGridPaneColumnConstraints();
        setGridPaneColumnConstraints();
        setGridPaneRowConstraints();
        setGridPaneRowConstraints();
        setGridPaneHAndV();

    }

    /**
     * used to set the length of a vertical and horizontal gridPane
     *
     */
    private void setGridPaneHAndV() {
        gridPane.setVgap(GAP_LENGTH);
        gridPane.setHgap(GAP_LENGTH);
    }

    /**
     * Initialize Gridpane column constraints
     */
    private void setGridPaneColumnConstraints() {
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
    }


    /**
     * Initialize Gridpane row constraints
     */
    private void setGridPaneRowConstraints() {
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
    }

    /**
     * Initialize Anchorpane
     */
    private void setAnchorPane() {
        AnchorPane.setTopAnchor(gridPane, 0d);
        AnchorPane.setLeftAnchor(gridPane, 0d);
        AnchorPane.setBottomAnchor(gridPane, 0d);
        AnchorPane.setRightAnchor(gridPane, 0d);
    }

}


