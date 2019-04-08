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
    private SourceManager source_manager = new SourceManager("./article_db");
    @FXML
    private Button cancel_button;
    @FXML
    private Button confirm_button;
    @FXML
    private ListView list_view_sources;

    public SourceMenu() throws IOException {
    }

    public void initialize() throws IOException {
        /**
         * Constructor of the Menu
         * @throws IOException : if there's no source
         */
        list_view_sources.setCellFactory(lv -> new SourceCell());
        SourceManager source_manager = new SourceManager("./article_db");
        display_sources(source_manager.load_sources());
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage primaryStage)  {
        /*
        Set the window of the source menu
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/be/ac/ulb/infof307/g04/view/SourceMenu.fxml"));
        try {
            AnchorPane main_container;
            main_container = loader.load();
            Scene scene = new Scene(main_container);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cancel() {
        /*
        cancel button of the menu
         */
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void confirm(){
        /*
        confirm button on the menu when adding sources,...
         */
        ObservableList<DatabaseSource> items_list = list_view_sources.getItems();
        //System.out.println(items_list);
        for (int i = 0; i < items_list.size(); i++) {
            source_manager.update_source(items_list.get(i));
        }
        Stage stage = (Stage) confirm_button.getScene().getWindow();
        stage.close();
    }

    public void display_sources(ArrayList<DatabaseSource> sources) {
        /*
        show all the sources
         */
        for (DatabaseSource item : sources) {
            list_view_sources.getItems().add(item);
        }
    }
}