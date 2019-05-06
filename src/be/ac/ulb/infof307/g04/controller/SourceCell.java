package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseSource;
import be.ac.ulb.infof307.g04.model.TagManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Class SourceCell where a source cell is created
 * @see DatabaseSource
 */

public class SourceCell extends ListCell<DatabaseSource>{


    private final GridPane gridPane = new GridPane();
    private final Label titleLabel = new Label();
    private final Label urlLabel = new Label();
    private final AnchorPane content = new AnchorPane();
    private final CheckBox sourceEnabled = new CheckBox();
    private final Spinner<Integer> sourceLifespan = new Spinner<>();
    private final Spinner<Integer> sourceNumberOfArticles = new Spinner<>();
    private final ComboBox<String> sourceTag = new ComboBox<>();
    private DatabaseSource item;

    public SourceCell() {
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1em;");
        sourceLifespan.setMaxWidth(Double.MAX_VALUE);
        sourceNumberOfArticles.setMaxWidth(Double.MAX_VALUE);
        sourceTag.setMaxWidth(Double.MAX_VALUE);

        initGridPane();
        initAnchorPane();
        content.getChildren().add(gridPane);

        sourceListener();

    }

    /**
     * update the sources
     * @see DatabaseSource
     * @param _item source that has to be modified
     * @param _empty check if the source is _empty or not
     */
    @Override
    protected void updateItem(DatabaseSource _item, boolean _empty) {
        item = _item;
        super.updateItem(item, _empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!_empty && item != null) {
            titleLabel.setText(item.getSourceName());
            urlLabel.setText(item.getUrl());
            updateSource();

            initTag();

            setText(null);
            setGraphic(content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);


        }
    }

    /**
     * Initialize the tags in the comboBox
     * @see TagManager
     * @throws java.io.IOException : if an article doesn't have a tag -> default
     */
    private void initTag() {
        TagManager tagManager = new TagManager("./article_db", "password");
        ObservableList<String> tags = FXCollections.observableArrayList();
        tagManager.getAll().forEach(tag -> tags.add(tag.getName()));
        sourceTag.setItems(tags);

        if(item.getTag() != null) {
            sourceTag.setValue(item.getTag());
        } else {
            sourceTag.setValue("Default");
        }
    }

    /**
     * Initialize gridpane
     */
    private void initGridPane(){
        initGridPaneConstraints();
        initGridPaneColumnConstraints();
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.getChildren().setAll(titleLabel, urlLabel, sourceEnabled, sourceNumberOfArticles, sourceLifespan, sourceTag);
    }

    /**
     * Initialize gridpane constraints
     */
    private void initGridPaneConstraints(){
        GridPane.setConstraints(titleLabel, 1, 0);
        GridPane.setConstraints(urlLabel, 2, 0);
        GridPane.setConstraints(sourceEnabled, 0, 0);
        GridPane.setConstraints(sourceNumberOfArticles, 3, 0);
        GridPane.setConstraints(sourceLifespan, 4, 0);
        GridPane.setConstraints(sourceTag, 5, 0);
    }

    /**
     * Initialize gridpane column constraints
     */
    private void initGridPaneColumnConstraints(){
        ColumnConstraints columnConstraints = new ColumnConstraints(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Priority.NEVER, HPos.LEFT, true);
        columnConstraints.setPercentWidth(5);
        ColumnConstraints defaultConstraint = new ColumnConstraints(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Priority.NEVER, HPos.LEFT, true);
        defaultConstraint.setPercentWidth(19);

        gridPane.getColumnConstraints().add(columnConstraints);
        gridPane.getColumnConstraints().add(defaultConstraint);
        gridPane.getColumnConstraints().add(defaultConstraint);
        gridPane.getColumnConstraints().add(defaultConstraint);
        gridPane.getColumnConstraints().add(defaultConstraint);
        gridPane.getColumnConstraints().add(defaultConstraint);
    }

    /**
     * Initialize anchorpane
     */
    private void initAnchorPane(){
        AnchorPane.setTopAnchor(gridPane, 0d);
        AnchorPane.setLeftAnchor(gridPane, 0d);
        AnchorPane.setBottomAnchor(gridPane, 0d);
        AnchorPane.setRightAnchor(gridPane, 0d);
    }

    /**
     * Gather all the listener source functions
     */
    private void sourceListener(){
        //listener that reacts when the checkbox value is modified
        sourceEnabled.selectedProperty().addListener((obs, oldValue, newValue) ->
                item.setEnabled(sourceEnabled.isSelected()));
        //listener that reacts when the left spinner value (number of articles to load) is modified
        sourceNumberOfArticles.valueProperty().addListener((obs, oldValue, newValue) ->
                item.setNumberToDownload(sourceNumberOfArticles.getValue()));
        //listener that reacts when the right spinner value (lifespan of an article) is modified
        sourceLifespan.valueProperty().addListener((obs, oldValue, newValue) ->
                item.setLifeSpanDefault(sourceLifespan.getValue()));
        //listener that reacts when the combobox value (tags) is modified
        sourceTag.valueProperty().addListener((obs, oldValue, newValue) ->
                item.setTag(sourceTag.getValue()));
    }

    /**
     * Does the update of the source
     */
    private void updateSource(){
        sourceEnabled.setSelected(item.isEnabled());
        SpinnerValueFactory<Integer> valueFactoryNumber = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, item.getNumberToDownload());
        sourceNumberOfArticles.setValueFactory(valueFactoryNumber);
        SpinnerValueFactory<Integer> valueFactoryLifespan = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, item.getLifeSpanDefault());
        sourceLifespan.setValueFactory(valueFactoryLifespan);
    }
}