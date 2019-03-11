package controller;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class ArticleCell extends ListCell<Article> {

    private final GridPane gridPane = new GridPane();
    private final ImageView articleIcon = new ImageView();
    private final Label titleLabel = new Label();
    private final Label descriptionLabel = new Label();
    private final Label linkLabel = new Label();
    private final AnchorPane content = new AnchorPane();

    public ArticleCell() {
        articleIcon.setFitWidth(75);
        articleIcon.setPreserveRatio(true);
        GridPane.setConstraints(articleIcon, 0, 0, 1, 2);
        GridPane.setValignment(articleIcon, VPos.TOP);
        //
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1em;");
        GridPane.setConstraints(titleLabel, 1, 0);
        //
        descriptionLabel.setStyle("-fx-font-size: 0.9em; -fx-font-style: italic; -fx-opacity: 0.5;");
        GridPane.setConstraints(descriptionLabel, 2, 0, 1, 1);
        //
        GridPane.setConstraints(linkLabel, 1, 1, 2, 1);

        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.getChildren().setAll(articleIcon, titleLabel, descriptionLabel, linkLabel);
        AnchorPane.setTopAnchor(gridPane, 0d);
        AnchorPane.setLeftAnchor(gridPane, 0d);
        AnchorPane.setBottomAnchor(gridPane, 0d);
        AnchorPane.setRightAnchor(gridPane, 0d);
        content.getChildren().add(gridPane);
    }


    @Override
    protected void updateItem(Article item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!empty && item != null) {
            titleLabel.setText(item.getTitle());
            descriptionLabel.setText(item.getDescription());
            //articleIcon.setImage(item.());
            linkLabel.setText(item.getLink());
            setText(null);
            setGraphic(content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}