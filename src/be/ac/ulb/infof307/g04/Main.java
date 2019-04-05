package be.ac.ulb.infof307.g04;


import be.ac.ulb.infof307.g04.controller.*;
import be.ac.ulb.infof307.g04.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;


public class Main extends Application {

    @FXML
    private ListView<Article> list_view_articles;

    @FXML
    private MenuItem quit_button;

    @FXML
    private  GridPane grid_pane;

    private static ArticleManager article_manager;
    private static SourceManager source;

    private ToolBar search_bar;
    private Button close_search_button;
    private TextField search_field;
    private Label match_count;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/LoginRegister.fxml"));
        Parent loginroot = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(loginroot));
        stage.showAndWait();

    }

    @FXML
    public void initialize() {
    }

};