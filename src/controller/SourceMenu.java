package controller;

import com.sun.glass.ui.View;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javax.xml.transform.Source;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class SourceMenu extends Application {
    @FXML
    private VBox sources_list_vbox;
    @FXML
    private MenuButton number_button;
    @FXML
    private MenuButton lifespan_button;
    @FXML
    private Button apply_button;
    @FXML
    private Button cancel_button;
    @FXML
    private Button ok_button;
    @FXML
    private ListView list_view_sources;

    private int numberof_articles = 5;
    private int temporary_number ;
    private int lifespanof_articles = 5;
    private int temporary_lifespan ;
    private List<CheckBox> checkboxes = new ArrayList<>();
    //private List<Source> all_sources = new ArrayList<>();
    //private List<Source> chosen_sources = new ArrayList<>();
    //private SourceModel model = new SourceModel();
    private List<String> chosen_numbers = new ArrayList<String>();

    public SourceMenu(List<Source> _all_sources) throws IOException {
        for (int i = 0; i < _all_sources.size(); i++){
            //list_view_sources.getItems().add();
        }
    }

    public void initialize() throws IOException {
        //numberof_articles = model.get_articles_persource();
        //lifespanof_articles = model.get_articles_lifespan();
        //chosen_numbers = model.get_chosen_numbers();
        //numbersToSources(chosen_numbers);
        //setCheckboxes();
        //setMenuButtons();
        list_view_sources.setCellFactory(lv -> new ArticleCell());
        display_sources();
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage primaryStage)  {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/view/SourceMenu.fxml"));
        System.out.println(loader.getLocation());
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

    public void cancel() {
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }

    public void ok() throws FileNotFoundException, UnsupportedEncodingException {
        //save_informations();
        Stage stage = (Stage) ok_button.getScene().getWindow();
        stage.close();
    }

    public void display_sources(ArrayList<Source> source) {
        list_view_sources.getItems().clear();
        for (Article item : source) {
            list_view_sources.getItems().add(item);
        }
    }


}
