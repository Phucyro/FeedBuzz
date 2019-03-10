package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SourceMenu extends Application {

    public static void main(String[] args) { launch(args); }
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

    private int numberof_articles = 5;
    private int temporary_number ;
    private int lifespanof_articles = 5;
    private int temporary_lifespan ;
    private CheckBox[] checkboxes = new CheckBox[10];
    private Source[] all_sources = new Source[10];
    private List<Source> chosen_sources = new ArrayList<Source>();


    @FXML
    private void set_number(ActionEvent event) {
        String input = ((MenuItem) event.getSource()).getText();
        temporary_number = Integer.parseInt(input);
        number_button.setText(input + " articles par source");
        event.consume();
    }

    @FXML
    private void set_lifespan(ActionEvent event){
        String input = ((MenuItem) event.getSource()).getText();
        temporary_lifespan = Integer.parseInt(input);
        lifespan_button.setText("articles valables pendant "+ ((MenuItem) event.getSource()).getText() + " jour(s) ");
        event.consume();
    }
    public void saveInformations(){
        numberof_articles = temporary_number;
        lifespanof_articles = temporary_lifespan;
        for (int i = 0; i < 10; i++){
            if (checkboxes[i].isSelected()){
                if (!chosen_sources.contains(all_sources[i])){
                    chosen_sources.add(all_sources[i]);
                }
            }
            else if (!checkboxes[i].isSelected()){
                if (chosen_sources.contains(all_sources[i])){
                    chosen_sources.remove(all_sources[i]);
                }
            }
        }
    }
    public void addSource(){

    }
    public void removeSource(){

    }
    public void cancel() {
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }
    public void start(Stage primaryStage)  {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/view/SourceMenu.fxml"));
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
    public void setCheckboxes() {
        for (int i = 0; i < 10; i++) {
            checkboxes[i] = new CheckBox();
            checkboxes[i].setText(all_sources[i].getName());
            sources_list_vbox.getChildren().add(checkboxes[i]);
            if (chosen_sources.contains(all_sources[i])){
                checkboxes[i].setSelected(true);
            }
        }
    }
    public void setMenuButtons(){
        number_button.setText(numberof_articles + " articles par source");
        lifespan_button.setText("articles valables pendant " + lifespanof_articles +" jour(s)");
    }
    public void initialize() {
        createSources();
        createSelectedSources();
        setCheckboxes();
        setMenuButtons();
    }
    public void createSources(){
        all_sources[0] = new Source("Le Monde");
        all_sources[1] = new Source("Le Soir");
        all_sources[2] = new Source("7sur7");
        all_sources[3] = new Source("l'avenir");
        all_sources[4] = new Source("Vice");
        all_sources[5] = new Source("The Huffington Post");
        all_sources[6] = new Source("New York daily");
        all_sources[7] = new Source("CNN");
        all_sources[8] = new Source("Buzzfeed");
        all_sources[9] = new Source("Science Daily");
    }
    public void createSelectedSources(){
        for (int i = 0; i<all_sources.length; i++){
            chosen_sources.add(all_sources[i]);
        }
        chosen_sources.remove(all_sources[4]);
        chosen_sources.remove(all_sources[7]);
        chosen_sources.remove(all_sources[2]);
    }
}
