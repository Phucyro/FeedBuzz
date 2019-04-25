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
    private SourceManager sourceManager = new SourceManager("./article_db");
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    @FXML
    private ListView listViewSources;

    public SourceMenu() throws IOException {
    }

    public void initialize() throws IOException {
        /**
         * Constructor of the Menu
         * @throws IOException : if there's no source
         */
        listViewSources.setCellFactory(lv -> new SourceCell());
        SourceManager source_manager = new SourceManager("./article_db");
        displaySources(source_manager.loadSources());
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage _primaryStage) throws IOException {
        /*
        Set the window of the source menu
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/be/ac/ulb/infof307/g04/view/SourceMenu.fxml"));
        AnchorPane main_container;
        main_container = loader.load();
        _primaryStage.setTitle("Source Menu");
        Scene scene = new Scene(main_container);
        _primaryStage.setScene(scene);
        _primaryStage.show();

    }

    @FXML
    public void cancel() {
        /*
        cancel button of the menu
         */
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void confirm(){
        /*
        confirm button on the menu when adding sources,...
         */
        ObservableList<DatabaseSource> items_list = listViewSources.getItems();
        //System.out.println(items_list);
        for (int i = 0; i < items_list.size(); i++) {
            sourceManager.updateSource(items_list.get(i));
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void displaySources(ArrayList<DatabaseSource> _sources) {
        /*
        show all the _sources
         */
        for (DatabaseSource item : _sources) {
            listViewSources.getItems().add(item);
        }
    }
}