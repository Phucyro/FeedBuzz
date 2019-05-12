package be.ac.ulb.infof307.g04.view;

import be.ac.ulb.infof307.g04.model.DatabaseTag;
import be.ac.ulb.infof307.g04.model.TagManager;
import io.jsondb.InvalidJsonDbApiUsageException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Class TagMenuController when all the tags are displayed. We can assign tag to a certain source
 * @see TagManager
 */


public class TagMenuController extends Application {
    private final TagManager tagManager;
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


    TagMenuController(String _dbPath, String _password) {
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
        TextInputDialog dialog = initInputDialog("Add", "Add a tag", "Please enter the name of the new tag");
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
     * @return return the dialog with all the fields initialized
     * @param _titleText title of the window
     * @param _headerText header of the window
     * @param _contextText description of the action
     */
    private TextInputDialog initInputDialog(String _titleText, String _headerText, String _contextText) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(_titleText);
        dialog.setHeaderText(_headerText);
        dialog.setContentText(_contextText);
        return dialog;
    }

    /**
     * alert message when encoding problem
     */
    private void alertDialog() {
        displayErrorWindow("Error!", "New tag name empty", "Please enter a name for the new tag");
    }


    /**
     * Create a dialog window to modify the name of the tag selected
     */
    @FXML
    public void modify() {
        if(tagsListview.getSelectionModel().getSelectedItem() == null){
            displayErrorWindow("Error!", "Selection error!", "No tag selected!");
        }
        else {
            DatabaseTag oldTag = new DatabaseTag();
            DatabaseTag newTag = new DatabaseTag();
            oldTag.setName(tagsListview.getSelectionModel().getSelectedItem());

            TextInputDialog dialog = initInputDialog("Modify tag", "Modification of a tag name",
                        "Change the tag " + "\"" + tagsListview.getSelectionModel().getSelectedItem() + "\" to: ");
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
            displayErrorWindow("Error!", "Selection error!", "No tag selected!");
        }
        else {
            try {
                DatabaseTag tag = new DatabaseTag();
                tag.setName(tagsListview.getSelectionModel().getSelectedItem());
                tagManager.deleteTag(tag);
                initList();
            } catch (InvalidJsonDbApiUsageException e){
                displayErrorWindow("Error!", "Tag removal error!", "An error happened while deleting tag, please try again");
            }
        }
    }

    /**
     * Cases where there might be an error
     * @see Alert
     */
    private void displayErrorWindow(String _title, String _header, String _content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(_title);
        alert.setHeaderText(_header);
        alert.setContentText(
                _content);
        alert.showAndWait();
    }
}