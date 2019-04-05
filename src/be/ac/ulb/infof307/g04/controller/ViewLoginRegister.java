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
        Stage user_agreement_view = new Stage();
        // set title for the stage
        user_agreement_view.setTitle("Contrat de license");

        // create a webview object
        WebView w = new WebView();

        // get the web engine
        WebEngine wsk = w.getEngine();

        // load a website
        // temporaire, j'ai eu des soucis avec le chargement du document html local dans webview_html
        wsk.loadContent("<!DOCTYPE html>\n" +
                "<p>&nbsp;</p>\n" +
                "<h1 style=\"text-align: center;\"><strong>User agreement and services terms here</strong></h1>\n" +
                "<p>----------------------------------------------------------------------------------------------------------------------------------------------</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>Here are the conditions the user have to comply in order to user our services :</p>\n" +
                "<ul>\n" +
                "\t<li>DO NOT modify any data of the app folder</li>\n" +
                "\t<li>Ask for permission if you intend to reuse some parts of the application</li>\n" +
                "</ul>\n" +
                "<p>&nbsp;</p>","text/html");


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


    public void launch_main_app(String username){

    }

    public void make_user_directory(File file){
        if (file.mkdir()) {
            System.out.println("Successfully created " + file.getAbsolutePath());
        } else {
            System.out.println("Failed creating " + file.getAbsolutePath());
        }
    }


    public void make_json_file(File file) throws java.io.IOException{
        FileWriter fwriter = new FileWriter(file);
        fwriter.write("{\"schemaVersion\":\"1.0\"}");

        fwriter.close();
    }



    public void set_warning_and_display(Label label_warning, String warning){
        label_warning.setText(warning);
        label_warning.setVisible(true);
    }


    public void connect_button_pressed() throws java.io.IOException{
        login_warning.setVisible(false);
        String username_str = login_username.getText();
        String password_str = login_password.getText();


        if(!username_str.isEmpty() && !password_str.isEmpty()){
            if(user_manager.existUser(username_str,password_str)){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleList.fxml"));
                ViewListArticles controller = new ViewListArticles("./article_db/"+username_str);
                loader.setController(controller);
                Parent loginroot = (Parent) loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(loginroot));
                stage.show();
            }

            else{ set_warning_and_display(login_warning, "Nom d'utilisateur ou mot de passe invalide"); }

        }
        else{  set_warning_and_display(login_warning, "Les champs ne peuvent etre vides"); }
    }




    public void register_button_pressed() throws java.io.IOException{



        register_warning.setVisible(false);

        String username_str = register_username.getText();
        String password_str = register_password.getText();
        String confirm_password_str = register_confirm_password.getText();


        if(username_str.length() >= 5 && username_str.length() <=17){
            if(password_str.length() >= 5 && password_str.length() <= 17){
                if(password_str.equals(confirm_password_str)){
                    if(user_agreement_checkbox.isSelected()){
                        if(!user_manager.existUsername(username_str)){
                            System.out.println("we can add user");

                            user_manager.add_user(username_str,password_str);
                            File file = new File("./article_db/"+username_str);
                            if(file.exists()) {
                                file.delete();
                            }
                            make_user_directory(file);
                            file = new File("./article_db/"+username_str+"/articles.json");
                            if(file.exists()) {
                                file.delete();
                            }
                            make_json_file(file);
                            file = new File("./article_db/"+username_str+"/tags.json");
                            if(file.exists()) {
                                file.delete();
                            }
                            make_json_file(file);
                            file = new File("./article_db/"+username_str+"/sources.json");
                            if(file.exists()) {
                                file.delete();
                            }
                            make_json_file(file);

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleList.fxml"));
                            ViewListArticles controller = new ViewListArticles("./article_db/"+username_str);
                            loader.setController(controller);
                            Parent loginroot = (Parent) loader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(loginroot));
                            stage.show();
                            login_username.getScene().getWindow().hide();



                        }
                        else{set_warning_and_display(register_warning,"Nom d'utilisateur déja pris"); }
                    }
                    else{set_warning_and_display(register_warning, "Vous devez accepter les termes d'utilisation pour vous inscrire"); }
                }
                else{set_warning_and_display(register_warning, "Les mots de passe ne correspondent pas");}
            }
            else{ set_warning_and_display(register_warning, "Le mot de passe doit etre compris entre 5 et 22 caracteres");};
        }
        else{set_warning_and_display(register_warning, "Le nom d'utilisateur doit etre compris entre 5-17 caractères et seuls ces caracteres speciaux sont autorisés : '-_$/'");}


    }


}