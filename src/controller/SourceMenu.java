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
    private List<CheckBox> checkboxes = new ArrayList<>();
    private List<Source> all_sources = new ArrayList<>();
    private List<Source> chosen_sources = new ArrayList<>();


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
    public void save_informations(){
        numberof_articles = temporary_number;
        lifespanof_articles = temporary_lifespan;
        for (int i = 0; i < 10; i++){
            if (checkboxes.get(i).isSelected()){
                if (!chosen_sources.contains(all_sources.get(i))){
                    chosen_sources.add(all_sources.get(i));
                }
            }
            else if (!checkboxes.get(i).isSelected()){
                if (chosen_sources.contains(all_sources.get(i))){
                    chosen_sources.remove(all_sources.get(i));
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
            if (chosen_sources.contains(all_sources.get(i))){
                checkboxes.get(i).setSelected(true);
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
