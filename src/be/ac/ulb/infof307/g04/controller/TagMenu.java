package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseTag;
import be.ac.ulb.infof307.g04.model.TagManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class TagMenu  extends Application {
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


    @FXML
    private ListView<String> tags_listview;


    public TagMenu() throws IOException {
    }

    public void initialize() throws IOException {
        init_list();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

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

    private void init_list() {
        ObservableList<String> tags = FXCollections.observableArrayList();
        for (DatabaseTag tag : tag_manager.get_all()) {
            if (!tag.getName().equals("Default")) {
                tags.add(tag.getName());
            }
        }
        tags_listview.setItems(tags);
    }


    /**
     * Create a dialog window to add a new tag
     */
    @FXML
    public void add() {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Ajout");
        dialog.setHeaderText("Ajout d'un tag");
        dialog.setContentText("Veuillez entrer le tag à ajouter: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DatabaseTag tag = new DatabaseTag();
            tag.setName(result.get());
            tag_manager.add_tag(tag);
        }
        init_list();
    }

    /**
     * Create a dialog window to modify the name of the tag selected
     */
    @FXML
    public void modify() {
        if(tags_listview.getSelectionModel().getSelectedItem() == null){
            display_error_window();
        }
        else {
            DatabaseTag oldTag = new DatabaseTag();
            DatabaseTag newTag = new DatabaseTag();
            oldTag.setName(tags_listview.getSelectionModel().getSelectedItem());


            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Modification");
            dialog.setHeaderText("Modification d'un tag");
            dialog.setContentText("En quoi voulez-vous modifier le tag "+"\""+tags_listview.getSelectionModel().getSelectedItem()+"\"? ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                newTag.setName(result.get());
                tag_manager.modify_tag(oldTag, newTag);
            }
            init_list();
        }
    }

    @FXML
    public void delete() {
        if(tags_listview.getSelectionModel().getSelectedItem() == null){
            display_error_window();
        }
        else {
            DatabaseTag tag = new DatabaseTag();
            tag.setName(tags_listview.getSelectionModel().getSelectedItem());
            tag_manager.delete_tag(tag);
            init_list();
        }
    }

    private void display_error_window(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erreur!");
        alert.setHeaderText("Erreur de sélection!");
        alert.setContentText("Vous n'avez pas selectionné de tag!");
        alert.showAndWait();
    }
    /*private void init_combo() {
        ObservableList<String> tags = FXCollections.observableArrayList();
        for (DatabaseTag tag : tag_manager.get_all()){
            if (!tag.getName().equals("Default")) {
                tags.add(tag.getName());
            }
        }
        combo_tags.setItems(tags);
        combo_tags.setValue(tags.get(0));
    }*/
}