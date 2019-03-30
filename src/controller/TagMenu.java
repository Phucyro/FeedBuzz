package controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.DatabaseSource;
import model.DatabaseTag;
import model.SourceManager;
import model.TagManager;

import java.io.IOException;
import java.util.ArrayList;

public class TagMenu  extends Application{
    private TagManager tag_manager = new TagManager("./article_db", "password");
    @FXML
    private Button add_button;
    @FXML
    private Button delete_button;
    @FXML
    private Button modify_button;
    @FXML
    private TextField text_add;
    @FXML
    private ComboBox combo_tags;

    public TagMenu() throws IOException {
    }

    public void initialize() throws IOException {
        init_combo();
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage primaryStage)  {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/view/TagMenu.fxml"));
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
    public void add(){
        DatabaseTag tag = new DatabaseTag();
        tag.setName(text_add.getText());
        tag_manager.add_tag(tag);
        init_combo();
    }

    private void init_combo() {
        ObservableList<String> tags = FXCollections.observableArrayList();
        for (DatabaseTag tag : tag_manager.get_all()){
            if (!tag.getName().equals("Default")) {
                tags.add(tag.getName());
            }
        }
        combo_tags.setItems(tags);
        combo_tags.setValue(tags.get(0));
    }

    @FXML
    public void modify(){

    }
    @FXML
    public void delete(){
        DatabaseTag tag = new DatabaseTag();
        tag.setName((String) combo_tags.getValue());
        tag_manager.delete_tag(tag);
        init_combo();
    }



}