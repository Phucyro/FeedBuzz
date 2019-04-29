package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.ViewListArticles;
import be.ac.ulb.infof307.g04.model.UserManager;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class ViewLoginRegister extends Application{

    static final int MIN_CHARACTERS = 5;
    static final int MAX_CHARACTERS = 17;
    UserManager user_manager = new UserManager("./article_db","password");


    @FXML
    private TextField login_username;
    @FXML
    private PasswordField login_password;
    @FXML
    private Label login_warning;


    @FXML
    private TextField register_username;
    @FXML
    private PasswordField register_password;
    @FXML
    private PasswordField register_confirm_password;
    @FXML
    private CheckBox user_agreement_checkbox;
    @FXML
    private Label register_warning;
    @FXML
    private Hyperlink user_agreement_link;

    private String DB_ROOT;

    private Stage main_stage;




    public ViewLoginRegister(){ }



    /**
     * Start javafx window
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
    }

    /**
     * Set the webview for the user agreement terms
     *
     */
    public void initialize(){
        DB_ROOT = "./article_db/";
        Stage user_agreement_view = new Stage();
        // set title for the stage
        user_agreement_view.setTitle("Contrat de license");

        // create a webview object
        WebView w = new WebView();

        // get the web engine
        WebEngine wsk = w.getEngine();

        // load the html page containing the user terms (possibility to format with css and html to make a pretty user agreements terms
        wsk.load(getClass().getResource("/be/ac/ulb/infof307/g04/html/userterms.html").toExternalForm());


        // create a scene
        Scene scene = new Scene(w, w.getPrefWidth(),
                w.getPrefHeight());

        // set the scene
        user_agreement_view.setScene(scene);
        // lorsque l'utilisateur clique sur le lien vers le contrat d'utilisation
        user_agreement_link.setOnAction(e -> {
            user_agreement_view.show();
        });

    }




    /**
     * Make a directory to contain the user database (articles,sources and tags)
     * @param username article the name of the folder to make
     */
    public void make_user_directory(String username){
        File file = new File(DB_ROOT + username);
        if (file.exists()) { file.delete();}

        if (file.mkdir())  {
            System.out.println("Successfully made folder" + file.getAbsolutePath());
        } else {
            System.out.println("Failed making the folder " + file.getAbsolutePath());
        }
    }




    /**
     * Make a json file and write the header
     * @param path the path to the user folder database (contain the json filename)
     */
    public void make_json_file(String path) throws java.io.IOException{
        File file = new File(path);
        if(file.exists()) {
            file.delete();
        }
        FileWriter fwriter = new FileWriter(file);
        fwriter.write("{\"schemaVersion\":\"1.0\"}");
        fwriter.close();
    }



    /**
     * Update the label to inform the user the input are not valid in the login or register form
     * @param label_warning the label used to display the warning
     * @param warning the message warning
     */
    public void set_warning_and_display(Label label_warning, String warning){
        label_warning.setText(warning);
        label_warning.setVisible(true);
    }




    /**
     * check the validity of the textfield inputs in the login form
     * @param username_str the username
     * @param password_str the password
     */
    public boolean login_inputs_valids(String username_str, String password_str) {
        if (!username_str.isEmpty() && !password_str.isEmpty()) {
            return true;
        }
        else{
            set_warning_and_display(login_warning, "Les champs ne peuvent etre vides");
            //return false;
            return true;
        }
    }


    /**
     * check the validity of the inputs in the register form
     * @param username_str the username
     * @param password_str the password
     * @param confirm_password_str the confirmation password
     */
    public boolean register_inputs_valids(String username_str, String password_str, String confirm_password_str) {
        // on peut se connecter directement en cliquant sur connecter apres avoir register un user, temporaire pour faciliter la tache
        if(username_str.length() >= 0 && username_str.length() <=17){
            if(password_str.length() >= 0 && password_str.length() <= 17){
                if(password_str.equals(confirm_password_str)){
                    if(user_agreement_checkbox.isSelected()){
                        return true;
                    }
                    else{set_warning_and_display(register_warning, "Vous devez accepter les termes d'utilisation pour vous inscrire"); }
                }
                else{set_warning_and_display(register_warning, "Les mots de passe ne correspondent pas");}
            }
            else{ set_warning_and_display(register_warning, "Le mot de passe doit etre compris entre 5 et 22 caracteres");};
        }
        else{set_warning_and_display(register_warning, "Le nom d'utilisateur doit etre compris entre 5-17 caractères et seuls ces caracteres speciaux sont autorisés : '-_$/'");}

        return false;

    }



    /**
     * Try to connect the user
     */
    public void connect_button_pressed() throws java.io.IOException {
        login_warning.setVisible(false);
        String username_str = login_username.getText();
        String password_str = login_password.getText();

        if (login_inputs_valids(username_str,password_str)) {
            if (user_manager.existUser(username_str, password_str)) {
                launch_main_app(DB_ROOT+username_str);
            }
            else{
                set_warning_and_display(login_warning, "Nom d'utilisateur ou mot de passe invalide");
            }
        }
    }



    public void app_closed(){
        main_stage.close();
    }
    /**
     * Try to register the user
     */
    public void register_button_pressed() throws java.io.IOException{

        register_warning.setVisible(false);
        String username_str = register_username.getText();
        String password_str = register_password.getText();
        String confirm_password_str = register_confirm_password.getText();

        if(register_inputs_valids(username_str,password_str,confirm_password_str)) {
            System.out.println("skkkkkkkk");
            if (!user_manager.existUsername(username_str)) {
                System.out.println("skkkkkkkk");
                user_manager.add_user(username_str, password_str);
                String db_user_path = DB_ROOT + username_str;

                make_user_directory(username_str);
                make_json_file(db_user_path + "/articles.json");
                make_json_file(db_user_path + "/tags.json");
                make_json_file(db_user_path + "/sources.json");
                launch_main_app(db_user_path);
            }
            else{ set_warning_and_display(register_warning, "Le nom d'utilisateur existe déja");}
        }
    }




    /**
     * launch the main menu of the app with the path to the connected user's database
     * @param db_path the path to the user database
     */
    public void launch_main_app(String db_path) throws java.io.IOException{
        Window current_window = login_warning.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleList.fxml"));
        ViewListArticles controller = new ViewListArticles(db_path);
        loader.setController(controller);
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        controller.setMainStage(stage);
        stage.show();


        current_window.hide();

        /*Window current_window = login_warning.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleList.fxml"));
        ViewListArticles controller = new ViewListArticles(db_path);
        loader.setController(controller);
        Parent loginroot = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(loginroot));

        stage.show();
        current_window.hide();*/


    }
}