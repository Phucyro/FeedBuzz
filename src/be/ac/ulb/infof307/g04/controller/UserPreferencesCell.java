package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseSource;
import be.ac.ulb.infof307.g04.model.DatabaseTag;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Class SourceCell where a source cell is created
 * @see DatabaseSource
 */

public class UserPreferencesCell extends ListCell<DatabaseTag>{

    public static final int GAP_LENGTH = 6;
    private DatabaseTag item;
    private final GridPane gridPane = new GridPane();
    private final Label tagLabel = new Label();
    private final AnchorPane content = new AnchorPane();
    private final Spinner<Integer> tagImportance = new Spinner<>();
    private final String dbPath;
    private final String dbPassword;

    /**
     * Constructor of a source
     * @param _dbPath path of the source in the database
     * @param _dbPassword path of the password in the database
     */
    public UserPreferencesCell(String _dbPath, String _dbPassword) {
        dbPath = _dbPath;
        dbPassword = _dbPassword;
        tagLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1em;");

        initGridPane();
        initAnchorPane();
        content.getChildren().add(gridPane);

        tagListener();
    }

    /**
     * update the sources
     * @see DatabaseSource
     * @param _item source that has to be modified
     * @param _empty check if the source is _empty or not
     */
    @Override
    protected void updateItem(DatabaseTag _item, boolean _empty) {
        item = _item;
        super.updateItem(item, _empty);
        setGraphic(null);
        setText(null);
        setContentDisplay(ContentDisplay.LEFT);
        if (!_empty && item != null) {
            tagLabel.setText(item.getName());
            initLabel();
            setText(null);
            setGraphic(content);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    /**
     * initialize the label
     */
    private void initLabel(){
        SpinnerValueFactory<Integer> valueFactoryNumber = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, item.getUserPreference());
        tagImportance.setValueFactory(valueFactoryNumber);
    }

    /**
     * Initialize gridpane
     */
    private void initGridPane(){
        initGridPaneConstraints();
        initGridPaneColumnConstraints();
        gridPane.getRowConstraints().add(new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.NEVER, VPos.CENTER, true));
        setGridPaneHAndV();
        gridPane.getChildren().setAll(tagLabel, tagImportance);
    }

    /**
     * used to set the length of a vertical and horizontal gridPane
     */
    private void setGridPaneHAndV() {
        gridPane.setVgap(GAP_LENGTH);
        gridPane.setHgap(GAP_LENGTH);
    }

    /**
     * Initialize gridpane constraints
     */
    private void initGridPaneConstraints(){
        GridPane.setConstraints(tagLabel, 0, 0);
        GridPane.setConstraints(tagImportance, 1, 0);
    }

    /**
     * Initialize gridpane column constraints
     */
    private void initGridPaneColumnConstraints(){
        ColumnConstraints tagNameConstraints = new ColumnConstraints(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Priority.NEVER, HPos.LEFT, true);
        tagNameConstraints.setPercentWidth(60);
        ColumnConstraints spinnerConstraints = new ColumnConstraints(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Region.USE_PREF_SIZE, Priority.NEVER, HPos.LEFT, true);
        spinnerConstraints.setPercentWidth(40);

        gridPane.getColumnConstraints().add(tagNameConstraints);
        gridPane.getColumnConstraints().add(spinnerConstraints);
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
    private void tagListener(){
        //listener that reacts when the left spinner value (number of articles to load) is modified
        tagImportance.valueProperty().addListener((obs, oldValue, newValue) ->
                updateTag(newValue));
    }

    /**
     * update a tag
     * @param _newValue new value of the tag
     */
    private void updateTag(int _newValue){
        item.setUserPreference(_newValue);
//        tagManager.
    }
}