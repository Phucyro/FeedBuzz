package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseSource;
import be.ac.ulb.infof307.g04.model.SourceManager;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class SourceMenu where all the sources are displayed
 * @see SourceManager
 */


public class SourceMenu extends Application {
    private SourceManager sourceManager;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    @FXML
    private ListView listViewSources;

    public SourceMenu(String _dbPath, String _password) {
        sourceManager = new SourceManager(_dbPath, _password);
    }

    public void initialize() {
        /**
         * Constructor of the Menu
         * @throws IOException : if there's no source
         */
        listViewSources.setCellFactory(lv -> new SourceCell());
        displaySources(sourceManager.loadSources());
    }

    public void start(Stage _primaryStage) {}

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
        ObservableList<DatabaseSource> itemsList = listViewSources.getItems();
        //System.out.println(itemsList);
        for (int i = 0; i < itemsList.size(); i++) {
            sourceManager.updateSource(itemsList.get(i));
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