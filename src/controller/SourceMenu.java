package controller;

import com.sun.glass.ui.View;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Callback;
import model.DatabaseSource;
import model.SourceManager;

import javax.xml.transform.Source;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


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
        list_view_sources.setCellFactory(lv -> new SourceCell());
        SourceManager source_manager = new SourceManager("./article_db");
        display_sources(source_manager.load_sources());
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage primaryStage)  {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/view/SourceMenu.fxml"));
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
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void confirm(){
        System.out.println("------------------");
        ObservableList<DatabaseSource> items_list = list_view_sources.getItems();
        System.out.println(items_list);
        for (int i = 0; i < items_list.size(); i++) {
            source_manager.update_source(items_list.get(i));
        }
        Stage stage = (Stage) confirm_button.getScene().getWindow();
        stage.close();
    }

    public void display_sources(ArrayList<DatabaseSource> sources) {
        for (DatabaseSource item : sources) {
            list_view_sources.getItems().add(item);
        }
    }
}