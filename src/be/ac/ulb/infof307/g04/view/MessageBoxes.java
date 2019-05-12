package be.ac.ulb.infof307.g04.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

class MessageBoxes {
    /**
     * show a eroor box with a message
     *
     * @param _errorMessage the error message to print
     */
    static void showErrorBox(String _errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(_errorMessage);

        alert.showAndWait();
    }

    static boolean showConfirmationBox(String _message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, _message, ButtonType.NO, ButtonType.YES);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }
}