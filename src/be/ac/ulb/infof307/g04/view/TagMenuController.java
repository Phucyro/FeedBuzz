package be.ac.ulb.infof307.g04.view;

import be.ac.ulb.infof307.g04.model.DatabaseTag;
import be.ac.ulb.infof307.g04.model.TagManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Class TagMenuController when all the tags are displayed. We can assign tag to a certain source
 * @see TagManager
 */


public class TagMenuController extends Application {
    private TagManager tagManager;
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


    public TagMenuController(String _dbPath, String _password) {
        tagManager = new TagManager(_dbPath, _password);
    }

    public void initialize(){
        initList();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage _primaryStage) {}

    /**
     * initialize the list of tags
     */
    private void initList() {
        ObservableList<String> tags = FXCollections.observableArrayList();
        for (DatabaseTag tag : tagManager.getAll()) {
            if (!tag.getName().equals("Default")) {
                tags.add(tag.getName());
            }
        }
        tagsListview.setItems(tags);
    }


    /**
     * * Create a dialog window to add a new tag
     */
    @FXML
    public void add() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Add");
        dialog.setHeaderText("Add a tag");
        dialog.setContentText("Please enter the name of the new tag");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()){
            DatabaseTag tag = new DatabaseTag();
            tag.setName(result.get());
            tagManager.addTag(tag);
        }
        else{
            alertDialog();
        }
        initList();
    }

    /**
     * alert message when encoding problem
     */
    private void alertDialog() {
        System.out.println("Pop up");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error!");
        alert.setHeaderText("New tag name empty");
        alert.setContentText("Please enter a name for the new tag");
        alert.showAndWait();
    }


    /**
     * Create a dialog window to modify the name of the tag selected
     */
    @FXML
    public void modify() {
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
                tagManager.modifyTag(oldTag, newTag);
            }
            else{
                alertDialog();
            }
            initList();
        }
    }

    /**
     * Create a dialog window to delete the name of the tag selected
     */
    @FXML
    public void delete() {
        if(tagsListview.getSelectionModel().getSelectedItem() == null){
            displayErrorWindow();
        }
        else {
            DatabaseTag tag = new DatabaseTag();
            tag.setName(tagsListview.getSelectionModel().getSelectedItem());
            tagManager.deleteTag(tag);
            initList();
        }
    }

    /**
     * Cases where there might be an error
     * @see Alert
     */
    private void displayErrorWindow(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erreur!");
        alert.setHeaderText("Erreur de sélection!");
        alert.setContentText("Vous n'avez pas selectionné de tag!");
        alert.showAndWait();
    }
}