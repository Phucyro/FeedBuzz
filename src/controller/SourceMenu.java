package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;


public class SourceMenu extends Application {

    public static void main(String[] args) { launch(args); }
    @FXML
    private ScrollPane source_panel;
    @FXML
    private VBox sources_list_vbox;

    @FXML
    private MenuButton sources_number;
    @FXML
    private MenuButton sources_lifespan;

    private int numberof_articles = 5;
    private int lifespanof_articles = 5;

    @FXML
    private void set_number(ActionEvent event) {
        String input = ((MenuItem) event.getSource()).getText();
        numberof_articles = Integer.parseInt(input);
        sources_number.setText(input + " articles par source");
        event.consume();
    }

    @FXML
    private void set_lifespan(ActionEvent event){
        String input = ((MenuItem) event.getSource()).getText();
        lifespanof_articles = Integer.parseInt(input);
        sources_lifespan.setText("articles valables pendant "+ ((MenuItem) event.getSource()).getText() + " jour(s) ");
        event.consume();
    }
    private void save_changes(ActionEvent event){

    }


    public void start(Stage primaryStage)  {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/view/SourceMenu.fxml"));
        try {
            AnchorPane conteneurPrincipal;
            conteneurPrincipal = loader.load();
            Scene scene = new Scene(conteneurPrincipal);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void initialize() {
        CheckBox[] checkboxes = new CheckBox[50];
        MenuItem[] lifespans = new MenuItem[5];
        lifespans = sources_lifespan.getItems().toArray(new MenuItem[0]);
        for (int i = 0; i < 50; i++) {
            //CheckBox checkbox = new CheckBox();
            checkboxes[i] = new CheckBox();
            checkboxes[i].setText(String.valueOf(i));
            sources_list_vbox.getChildren().add(checkboxes[i]);
        }
    }

}
