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

/**
 * Class TagMenu when all the tags are displayed. We can assign tag to a certain source
 * @see TagManager
 */


public class TagMenu  extends Application {
    private TagManager tag_manager = new TagManager("./article_db", "password");
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;
    @FXML
    private TextField textAdd;
    @FXML
    private ComboBox comboTags;


    @FXML
    private ListView<String> tagsListview;


    public TagMenu() throws IOException {
    }

    public void initialize() throws IOException {
        /**
         * @see init_list()
         */

        initList();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage _primaryStage) throws IOException {
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/be/ac/ulb/infof307/g04/view/TagMenu.fxml"));
        AnchorPane main_container;
        main_container = loader.load();
        Scene scene = new Scene(main_container);
        _primaryStage.setScene(scene);
        _primaryStage.show();*/
    }

    private void initList() {
        ObservableList<String> tags = FXCollections.observableArrayList();
        for (DatabaseTag tag : tag_manager.getAll()) {
            if (!tag.getName().equals("Default")) {
                tags.add(tag.getName());
            }
        }
        tagsListview.setItems(tags);
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
            tag_manager.addTag(tag);
        }
        else{
            System.out.println("Pop up");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("New tag name empty");
            alert.setContentText("Please enter a name for the new tag");
            alert.showAndWait();
        }
        initList();
    }


    @FXML
    public void modify() {
        /*
         * Create a dialog window to modify the name of the tag selected
         */

        if(tagsListview.getSelectionModel().getSelectedItem() == null){
            displayErrorWindow();
        }
        else {
            DatabaseTag oldTag = new DatabaseTag();
            DatabaseTag newTag = new DatabaseTag();
            oldTag.setName(tagsListview.getSelectionModel().getSelectedItem());


            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Modify tag");
            dialog.setHeaderText("Modification of a tag name");
            dialog.setContentText("Change the tag "+"\""+ tagsListview.getSelectionModel().getSelectedItem()+"\" to: ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().isEmpty()) {
                newTag.setName(result.get());
                tag_manager.modifyTag(oldTag, newTag);
            }
            else{
                System.out.println("Pop up");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText("New tag name empty");
                alert.setContentText("Please enter a name for the new tag");
                alert.showAndWait();
            }
            initList();
        }
    }

    @FXML
    public void delete() {
        /*
         * Create a dialog window to delete the name of the tag selected
         */
        if(tagsListview.getSelectionModel().getSelectedItem() == null){
            displayErrorWindow();
        }
        else {
            DatabaseTag tag = new DatabaseTag();
            tag.setName(tagsListview.getSelectionModel().getSelectedItem());
            tag_manager.deleteTag(tag);
            initList();
        }
    }

    private void displayErrorWindow(){
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
}