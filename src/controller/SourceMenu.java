package controller;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    private int numberof_articles = 5;
    private int lifespanof_articles = 5;
    CheckBox[] checkboxes = new CheckBox[10];
    Source[] all_sources = new Source[10];
    private List<Source> chosen_sources = new ArrayList<Source>();


    @FXML
    private void set_number(ActionEvent event) {
        String input = ((MenuItem) event.getSource()).getText();
        numberof_articles = Integer.parseInt(input);
        number_button.setText(input + " articles par source");
        event.consume();
    }

    @FXML
    private void set_lifespan(ActionEvent event){
        String input = ((MenuItem) event.getSource()).getText();
        lifespanof_articles = Integer.parseInt(input);
        lifespan_button.setText("articles valables pendant "+ ((MenuItem) event.getSource()).getText() + " jour(s) ");
        event.consume();
    }
    public List save_settings(ActionEvent event){
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
        return chosen_sources;
    }

    public void receiveinformations(Source[] sources,List<Source> selected_sources, int number, int lifespan){
        chosen_sources = selected_sources;
        numberof_articles = number;
        lifespanof_articles = lifespan;
    }
    public void addSource(){

    }
    public void removeSource(){

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
    public void setCheckboxes() {
        for (int i = 0; i < 10; i++) {
            checkboxes[i] = new CheckBox();
            checkboxes[i].setText(all_sources[i].getName());
            sources_list_vbox.getChildren().add(checkboxes[i]);
            if (chosen_sources.contains(all_sources[i])){
            //if (Arrays.asList(chosen_sources).contains(all_sources[i])) {
                checkboxes[i].setSelected(true);
            }
            checkboxes[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                }
            });
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
        System.out.println("all_sources " + all_sources);
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
