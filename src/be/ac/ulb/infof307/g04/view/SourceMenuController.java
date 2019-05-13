package be.ac.ulb.infof307.g04.view;

import be.ac.ulb.infof307.g04.controller.SourceCell;
import be.ac.ulb.infof307.g04.model.DatabaseSource;
import be.ac.ulb.infof307.g04.model.SourceManager;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Class SourceMenuController where all the sources are displayed
 *
 * @see SourceManager
 */


public class SourceMenuController extends Application {
    private final SourceManager sourceManager;
    private final String dbPath;
    private final String dbPassword;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    @FXML
    private ListView listViewSources;

    public SourceMenuController(String _dbPath, String _password) {
        dbPath = _dbPath;
        dbPassword = _password;
        sourceManager = new SourceManager(_dbPath, _password);
    }

    public void initialize() {
        /*
          Constructor of the Menu
          @throws IOException : if there's no source
         */
        listViewSources.setCellFactory(lv -> new SourceCell(dbPath, dbPassword));
        displaySources(sourceManager.loadSources());
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
    public void confirm() {
        ObservableList itemsList = listViewSources.getItems();
        for (Object anItemsList : itemsList) {
            sourceManager.updateSource((DatabaseSource) anItemsList);
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    /**
     * @param _sources list of all the sources
     */
    public void displaySources(ArrayList<DatabaseSource> _sources) {
        /*
        show all the _sources
         */
        for (DatabaseSource item : _sources) {
            listViewSources.getItems().add(item);
        }
    }
}