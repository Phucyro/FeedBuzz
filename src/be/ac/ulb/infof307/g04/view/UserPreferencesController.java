package be.ac.ulb.infof307.g04.view;

import be.ac.ulb.infof307.g04.controller.SourceCell;
import be.ac.ulb.infof307.g04.controller.UserPreferencesCell;
import be.ac.ulb.infof307.g04.model.DatabaseSource;
import be.ac.ulb.infof307.g04.model.DatabaseTag;
import be.ac.ulb.infof307.g04.model.TagManager;
import io.jsondb.InvalidJsonDbApiUsageException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Class TagMenuController when all the tags are displayed. We can assign tag to a certain source
 * @see TagManager
 */


public class UserPreferencesController extends Application {
    private final TagManager tagManager;

    private final String dbPath;
    private final String dbPassword;
    private final ArrayList<DatabaseTag> oldTagsList;


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
        oldTagsList = tagManager.getAll();
    }

    public void initialize() {
        /*
          Constructor of the Menu
          @throws IOException : if there's no source
         */
        tagsListview.setCellFactory(lv -> new UserPreferencesCell(dbPath, dbPassword));
        displayTags(tagManager.getAll());

    }

    public void displayTags(ArrayList<DatabaseTag> _tags) {
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
        ObservableList<DatabaseTag> newTagsList = tagsListview.getItems();
        for (int i = 0; i < newTagsList.size(); i++) {
            tagManager.modifyTag(oldTagsList.get(i), newTagsList.get(i));
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

}