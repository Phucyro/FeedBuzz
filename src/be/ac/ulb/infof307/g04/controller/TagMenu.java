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
import java.util.Comparator;
import java.util.Optional;

/**
 * Class TagMenu when all the tags are displayed. We can assign tag to a certain source
 * @see TagManager
 */


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
        /**
         * @see init_list()
         */

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


    @FXML
    public void add() {
        /*
         * Create a dialog window to add a new tag
         */
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Add");
        dialog.setHeaderText("Add a tag");
        dialog.setContentText("Please enter the name of the new tag");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()){
            DatabaseTag tag = new DatabaseTag();
            tag.setName(result.get());
            tag_manager.add_tag(tag);
        }
        else{
            System.out.println("Pop up");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("New tag name empty");
            alert.setContentText("Please enter a name for the new tag");
            alert.showAndWait();
        }
        init_list();
    }


    @FXML
    public void modify() {
        /*
         * Create a dialog window to modify the name of the tag selected
         */

        if(tags_listview.getSelectionModel().getSelectedItem() == null){
            display_error_window();
        }
        else {
            DatabaseTag oldTag = new DatabaseTag();
            DatabaseTag newTag = new DatabaseTag();
            oldTag.setName(tags_listview.getSelectionModel().getSelectedItem());


            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Modify tag");
            dialog.setHeaderText("Modification of a tag name");
            dialog.setContentText("Change the tag "+"\""+tags_listview.getSelectionModel().getSelectedItem()+"\"to: ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().isEmpty()) {
                newTag.setName(result.get());
                tag_manager.modify_tag(oldTag, newTag);
            }
            else{
                System.out.println("Pop up");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText("New tag name empty");
                alert.setContentText("Please enter a name for the new tag");
                alert.showAndWait();
            }
            init_list();
        }
    }

    @FXML
    public void delete() {
        /*
         * Create a dialog window to delete the name of the tag selected
         */
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
        /**
         * Cases where there might be an error
         * @see Alert
         */
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