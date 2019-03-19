package controller;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;



public class PreviewDisplay{

    //Fonction to preview an article
    public static void mouseOverArticle(GridPane articlePane, String summary) {

        //Pane that will contain the summary
        StackPane previewPane = new StackPane();
        previewPane.setPrefSize(250, 250);

        //Layouts options
        TextArea resume = new TextArea(summary);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        //Text area options
        resume.setWrapText(true);
        resume.setEditable(false);

        //Layout options
        vBox.getChildren().addAll(resume);
        previewPane.getChildren().addAll(vBox);

        Popup popup = new Popup();
        popup.getContent().add(previewPane);

        //Popup shows only when the mouse is over the article
        articlePane.hoverProperty().addListener((obs, oldVal, newValue) -> {

            if (newValue) {
                Bounds bounds = articlePane.localToScreen(articlePane.getLayoutBounds());
                double x = bounds.getMinX() - (previewPane.getWidth() / 2) + (articlePane.getWidth() / 2);
                double y = bounds.getMinY() - previewPane.getHeight();
                popup.show(articlePane, x, y);
            } else {
                popup.hide();
            }
        });
    }

}
