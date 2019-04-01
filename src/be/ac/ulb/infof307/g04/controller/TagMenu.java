package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseTag;
import be.ac.ulb.infof307.g04.model.TagManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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
        loader.setLocation(SourceMenu.class.getResource("/be/ac/ulb/infof307/g04/view/TagMenu.fxml"));
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
        TextInputDialog dialog = new TextInputDialog((String) combo_tags.getValue());
        DatabaseTag oldTag = new DatabaseTag();
        DatabaseTag newTag = new DatabaseTag();
        oldTag.setName((String) combo_tags.getValue());

        dialog.setHeaderText("Enter the new tagname:");
        dialog.setContentText("Tag name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            newTag.setName(name);
            tag_manager.modify_tag(oldTag, newTag);
        });
        init_combo();

    }
    @FXML
    public void delete(){
        DatabaseTag tag = new DatabaseTag();
        tag.setName((String) combo_tags.getValue());
        tag_manager.delete_tag(tag);
        init_combo();
    }



}