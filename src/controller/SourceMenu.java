package controller;

import com.sun.glass.ui.View;
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
import model.SourceModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SourceMenu extends Application {

    public SourceMenu() throws IOException {
    }

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
    @FXML
    private Button ok_button;

    private int numberof_articles = 5;
    private int temporary_number ;
    private int lifespanof_articles = 5;
    private int temporary_lifespan ;
    private List<CheckBox> checkboxes = new ArrayList<>();
    private List<Source> all_sources = new ArrayList<>();
    private List<Source> chosen_sources = new ArrayList<>();
    private SourceModel model = new SourceModel();
    private List<String> chosen_numbers = new ArrayList<String>();


    @FXML
    private void set_number(ActionEvent event) {
        String input = ((MenuItem) event.getSource()).getText();
        temporary_number = Integer.parseInt(input);
        number_button.setText(input);
        event.consume();
    }

    @FXML
    private void set_lifespan(ActionEvent event){
        String input = ((MenuItem) event.getSource()).getText();
        temporary_lifespan = Integer.parseInt(input);
        lifespan_button.setText(input);
        event.consume();
    }

    public void saveInformations() throws FileNotFoundException, UnsupportedEncodingException {
        if (chosen_sources.size() != 0) {
            chosen_sources.clear();
            chosen_numbers.clear();
        }
        if (temporary_number != 0) {
            numberof_articles = temporary_number;
        }
        if (temporary_lifespan != 0) {
            lifespanof_articles = temporary_lifespan;
        }
        for (int i = 0; i < all_sources.size(); i++) {
            if (checkboxes.get(i).isSelected()){
                if (!chosen_numbers.contains(i)) {
                    chosen_sources.add(all_sources.get(i));
                    chosen_numbers.add(Integer.toString(i));
                }
            }
            else if (!checkboxes.get(i).isSelected()){
                if (chosen_numbers.contains(i)) {
                    chosen_sources.remove(all_sources.get(i));
                    chosen_numbers.remove(Integer.toString(i));
                }
            }
        }
        model.applySettings(numberof_articles, lifespanof_articles, chosen_numbers);
    }


    public void addSource(){

    }
    public void removeSource(){

    }

    public void numbersToSources(List<String> numbers) {
        if (numbers.size() == 0) {
            System.out.println("here");
            chosen_sources.addAll(all_sources);
            chosen_numbers.add("0");
            chosen_numbers.add("1");
            chosen_numbers.add("2");
            chosen_numbers.add("3");
            chosen_numbers.add("4");
            chosen_numbers.add("5");
            chosen_numbers.add("6");
            chosen_numbers.add("7");
        } else {
            for (int i = 0; i < all_sources.size(); i++) {
                if (chosen_numbers.contains(Integer.toString(i))
                        && !chosen_sources.contains((all_sources.get(i)))) {
                    chosen_sources.add(all_sources.get((i)));
                }
            }
        }
    }

    public void cancel() {
        Stage stage = (Stage) ok_button.getScene().getWindow();
        stage.close();
    }

    public void ok() throws FileNotFoundException, UnsupportedEncodingException {
        saveInformations();
        Stage stage = (Stage) ok_button.getScene().getWindow();
        stage.close();
    }
    public void start(Stage primaryStage)  {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SourceMenu.class.getResource("/view/SourceMenu.fxml"));
        System.out.println(loader.getLocation());
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
        for (int i = 0; i < all_sources.size(); i++) {
            checkboxes.add(new CheckBox());
            checkboxes.get(i).setText(all_sources.get(i).getName());
            sources_list_vbox.getChildren().add(checkboxes.get(i));
            if (chosen_numbers.contains(Integer.toString(i))) {
                checkboxes.get(i).setSelected(true);
            } else {
                checkboxes.get(i).setSelected(false);
            }
            checkboxes.get(i).setPrefWidth(350.0);
            checkboxes.get(i).setPrefHeight(30.0);
        }
    }
    public void setMenuButtons(){
        number_button.setText(String.valueOf(numberof_articles));
        lifespan_button.setText(String.valueOf(lifespanof_articles));
    }

    public void initialize() throws IOException {
        createSources();
        //createSelectedSources();
        numberof_articles = model.get_articles_persource();
        lifespanof_articles = model.get_articles_lifespan();
        chosen_numbers = model.get_chosen_numbers();
        numbersToSources(chosen_numbers);
        setCheckboxes();
        setMenuButtons();
    }
    public void createSources(){
        all_sources.add(new Source("Le Monde", "https://www.lemonde.fr/rss/une.xml"));
        all_sources.add(new Source("Le Soir", "https://www.lesoir.be/rss/81853/cible_principale_gratuit"));
        all_sources.add(new Source("7sur7", "https://www.7sur7.be/rss.xml"));
        all_sources.add(new Source("Vice", "https://www.vice.com/fr_be/rss"));
        all_sources.add(new Source("The Huffington Post world news", "https://www.huffingtonpost.com/section/world-news/feed"));
        all_sources.add(new Source("Buzzfeed", "https://www.buzzfeed.com/index.xml"));
        all_sources.add(new Source("Science Daily", "https://www.sciencedaily.com/rss/all.xml"));
        all_sources.add(new Source("The Verge", "https://www.theverge.com/rss/index.xml"));

    }
    public void createSelectedSources(){
        for (int i = 0; i<all_sources.size(); i++){
            chosen_sources.add(all_sources.get(i));
        }
    }
}
