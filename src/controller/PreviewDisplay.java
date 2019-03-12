package controller;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class PreviewDisplay{


    //Fonction to preview an article. To update with actual article pane
    public static void mouseOverArticle(GridPane articlePane, String titre, String resume) {

        StackPane preview_pane = new StackPane();
        preview_pane.setPrefSize(200, 200);
        //stickyNotesPane.setStyle("-fx-background-color:  #20120F;");

        Label title = new Label(titre);
        TextArea summary = new TextArea(resume);
        VBox vertical_layout = new VBox();
        vertical_layout.setAlignment(Pos.CENTER);

        summary.setWrapText(true);
        summary.setEditable(false);

        vertical_layout.getChildren().addAll(title, summary);
        preview_pane.getChildren().addAll(vertical_layout);

        Popup popup = new Popup();
        popup.getContent().add(preview_pane);

        articlePane.hoverProperty().addListener((obs, oldVal, newValue) -> {
            if (newValue) {
                Bounds bnds = articlePane.localToScreen(articlePane.getLayoutBounds());
                double x = bnds.getMinX() - (preview_pane.getWidth() / 2) + (articlePane.getWidth() / 2);
                double y = bnds.getMinY() - preview_pane.getHeight();
                popup.show(articlePane, x, y);
            } else {
                popup.hide();
            }
        });
    }
}
