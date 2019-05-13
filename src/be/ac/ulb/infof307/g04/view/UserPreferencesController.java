package be.ac.ulb.infof307.g04.view;

import be.ac.ulb.infof307.g04.controller.UserPreferencesCell;
import be.ac.ulb.infof307.g04.model.DatabaseTag;
import be.ac.ulb.infof307.g04.model.TagManager;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Class TagMenuController when all the tags are displayed. We can assign tag to a certain source
 * @see TagManager
 */


public class UserPreferencesController extends Application {
    private final TagManager tagManager;

    private final String dbPath;
    private final String dbPassword;


    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ListView tagsListview;


    UserPreferencesController(String _dbPath, String _password) {
        dbPath = _dbPath;
        dbPassword = _password;
        tagManager = new TagManager(_dbPath, _password);
    }

    /**
     * Constructor of the Menu
     */
    public void initialize() {
        tagsListview.setCellFactory(lv -> new UserPreferencesCell(dbPath, dbPassword));
        displayTags(tagManager.getAll());

    }

    /**
     * display the tags
     * @param _tags tags to display
     */
    private void displayTags(ArrayList<DatabaseTag> _tags) {
        for (DatabaseTag item : _tags) {
            tagsListview.getItems().add(item);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage _primaryStage) {
    }

    /**
     * cancel button of the menu
     */
    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * confirm button on the menu when adding sources
     */
    @FXML
    public void confirm(){
        ObservableList newTagsList = tagsListview.getItems();
        for (Object aNewTagsList : newTagsList) {
            tagManager.modifyTag(aNewTagsList);
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

}