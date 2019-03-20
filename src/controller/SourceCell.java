package controller;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.DatabaseSource;


public class SourceCell extends ListCell<DatabaseSource>{


    private final GridPane gridPane = new GridPane();
    private final Label title_label = new Label();
    private final Label url_label = new Label();
    private final AnchorPane content = new AnchorPane();
    private final CheckBox source_enabled = new CheckBox();
    private final Spinner<Integer> source_lifespan = new Spinner<>();
    private final Spinner<Integer> source_number_of_articles = new Spinner<>();
    private final ComboBox<String> source_tag = new ComboBox<>();
    private DatabaseSource item;

    public SourceCell() {
        //
        title_label.setStyle("-fx-font-weight: bold; -fx-font-size: 1em;");
        GridPane.setConstraints(title_label, 1, 0);
        GridPane.setConstraints(url_label, 2, 0);
        GridPane.setConstraints(source_enabled, 0, 0);
        GridPane.setConstraints(source_number_of_articles, 3, 0);
        GridPane.setConstraints(source_lifespan, 4, 0);
        GridPane.setConstraints(source_tag, 5, 0);


        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT, true));
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.getChildren().setAll(title_label, url_label, source_enabled, source_number_of_articles, source_lifespan, source_tag);
        AnchorPane.setTopAnchor(gridPane, 0d);
        AnchorPane.setLeftAnchor(gridPane, 0d);
        AnchorPane.setBottomAnchor(gridPane, 0d);
        AnchorPane.setRightAnchor(gridPane, 0d);
        content.getChildren().add(gridPane);

        source_enabled.selectedProperty().addListener((obs, oldValue, newValue) ->
                item.setEnabled(source_enabled.isSelected()));//System.out.println(source_enabled.isSelected()));
        source_number_of_articles.valueProperty().addListener((obs, oldValue, newValue) ->
                item.setNumber_to_download(source_number_of_articles.getValue()));//System.out.println("New value: "+newValue));
        source_lifespan.valueProperty().addListener((obs, oldValue, newValue) ->
                item.setLifeSpan_default(source_lifespan.getValue()));//System.out.println("New value: "+newValue));

    }

    @Override
    protected void updateItem(DatabaseSource _item, boolean empty) {
        item = _item;
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!empty && item != null) {
            title_label.setText(item.getSource_name());
            url_label.setText(item.getUrl());
            source_enabled.setSelected(item.isEnabled());
            System.out.println(item.getSource_name());
            //System.out.println(item.getLifeSpan_default());
            SpinnerValueFactory<Integer> valueFactoryNumber = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, item.getNumber_to_download());
            source_number_of_articles.setValueFactory(valueFactoryNumber);
            SpinnerValueFactory<Integer> valueFactoryLifespan = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, item.getLifeSpan_default());
            source_lifespan.setValueFactory(valueFactoryLifespan);
            source_tag.setValue(item.getTag());

            setText(null);
            setGraphic(content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}