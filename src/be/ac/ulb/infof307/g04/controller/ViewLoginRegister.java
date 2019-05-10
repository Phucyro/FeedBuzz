package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.ArticleLabelizer;
import be.ac.ulb.infof307.g04.model.UserManager;
import be.ac.ulb.infof307.g04.view.ArticleListController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class ViewLoginRegister extends Application{

    static final int MIN_CHARACTERS = 5;
    static final int MAX_CHARACTERS = 17;
    private final String DB_ROOT = "./article_db/";
    final UserManager userManager = new UserManager("./article_db","password");


    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Label loginWarning;


    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField registerConfirmPassword;
    @FXML
    private CheckBox userAgreementCheckbox;
    @FXML
    private Label registerWarning;
    @FXML
    private Hyperlink userAgreementLink;

    private Stage mainStage;





    public ViewLoginRegister(){ }


    /**
     * Set the webview for the user agreement terms
     *
     */
    public void initialize(){
        Stage userAgreementView = new Stage();
        userAgreementView.setTitle("Contrat de license");


        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // load the html page containing the user terms
        webEngine.load(getClass().getResource("/be/ac/ulb/infof307/g04/html/userterms.html").toExternalForm());


        Scene scene = new Scene(webView, webView.getPrefWidth(),
                webView.getPrefHeight());

        userAgreementView.setScene(scene);
        userAgreementLink.setOnAction(e -> userAgreementView.show());

    }
    /**
     * Start javafx window
     */
    @Override
    public void start(Stage primaryStage) {
    }






    /**
     * Make a directory to contain the user database (articles,sources and tags)
     * @param _username article the name of the folder to make
     */
    public void makeUserDirectory(String _username){
        File file = new File(DB_ROOT + _username);
        if (file.exists()) { file.delete();}

        if (file.mkdir())  {
            System.out.println("Successfully made folder" + file.getAbsolutePath());
        } else {
            System.out.println("Failed making the folder " + file.getAbsolutePath());
        }
    }





    /**
     * Update the label to inform the user the input are not valid in the login or register form
     * @param _labelWarning the label used to display the warning
     * @param _warning the message warning
     */
    public void setWarningAndDisplay(Label _labelWarning, String _warning){
        if (_labelWarning!=null){
            _labelWarning.setText(_warning);
            _labelWarning.setVisible(true);
        }
    }




    /**
     * check the validity of the textfield inputs in the login form
     * @param _usernameStr the username
     * @param _passwordStr the password
     */
    public boolean loginInputsValid(String _usernameStr, String _passwordStr) {
        if (_usernameStr.length()!=14 || !_passwordStr.isEmpty()) {
            return true;
        }
        /*if (!_usernameStr.isEmpty() && !_passwordStr.isEmpty()) {
            return true;
        }*/
        else{
            setWarningAndDisplay(loginWarning, "Les champs ne peuvent etre vides");
            return false;
        }
    }


    /**
     * check the validity of the inputs in the register form
     * @param _usernameStr the username
     * @param _passwordStr the password
     * @param _confirmPasswordStr the confirmation password
     */
    public boolean registerInputsValid(String _usernameStr, String _passwordStr, String _confirmPasswordStr) {
        // on peut se connecter directement en cliquant sur connecter apres avoir register un user, temporaire pour faciliter la tache

        /*
        if(_usernameStr.length() >= MIN_CHARACTERS && _usernameStr.length() <=MAX_CHARACTERS){
            if(_passwordStr.length() >= MIN_CHARACTERS && _passwordStr.length() <= MAX_CHARACTERS){
                if(_passwordStr.equals(_confirmPasswordStr)){
                    return true;
                }
                else{
                    setWarningAndDisplay(registerWarning, "Les mots de passe ne correspondent pas");}
            }
            else{ setWarningAndDisplay(registerWarning, "Le mot de passe doit etre compris entre 5 et 22 caracteres");};
        }
        else{
            setWarningAndDisplay(registerWarning, "Le nom d'utilisateur doit etre compris entre 5-17 caractères et seuls ces caracteres speciaux sont autorisés : '-_$/'");}

        return false;

         */



        if(_usernameStr.length() >= 0 && _usernameStr.length() <=MAX_CHARACTERS){
            if(_passwordStr.length() >= 0 && _passwordStr.length() <= MAX_CHARACTERS){
                if(_passwordStr.equals(_confirmPasswordStr)){
                    return true;
                }
                else{
                    setWarningAndDisplay(registerWarning, "Les mots de passe ne correspondent pas");}
            }
            else{ setWarningAndDisplay(registerWarning, "Le mot de passe doit etre compris entre 5 et 22 caracteres");};
        }
        else{
            setWarningAndDisplay(registerWarning, "Le nom d'utilisateur doit etre compris entre 5-17 caractères et seuls ces caracteres speciaux sont autorisés : '-_$/'");}

        return false;

    }


    public boolean isCheckedUserAgreements(){
        if(userAgreementCheckbox.isSelected()){
            return true;
        }

        else{
            setWarningAndDisplay(registerWarning, "Vous devez accepter les termes d'utilisation pour vous inscrire");
            return false;
        }

    }


    /**
     * Try to connect the user
     */
    public void connectButtonPressed() throws java.io.IOException {
        loginWarning.setVisible(false);
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (loginInputsValid(username,password)) {
            if (userManager.existUser(username, password)) {
                launchMainApp(DB_ROOT+username, password);
            }
            else{
                setWarningAndDisplay(loginWarning, "Nom d'utilisateur ou mot de passe invalide");
            }
        }
    }




    public void appClosed(){
        mainStage.close();
    }

    /**
     * Try to register the user
     */
    public void registerButtonPressed() throws IOException {

        registerWarning.setVisible(false);
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();

        if(registerInputsValid(username,password,confirmPassword) && isCheckedUserAgreements()) {
            if (!userManager.existUsername(username)) {
                userManager.addUser(username, password);
                String db_user_path = DB_ROOT + username;

                makeUserDirectory(username);
                launchMainApp(db_user_path, password);
            }
            else{ setWarningAndDisplay(registerWarning, "Le nom d'utilisateur existe déja");}
        }
    }




    /**
     * launch the main menu of the app with the path to the connected user's database
     * @param _dbPath the path to the user database
     */
    public void launchMainApp(String _dbPath, String _password) throws java.io.IOException{
        Window currentWindow = loginWarning.getScene().getWindow();

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g04/view/ArticleListController.fxml"));
        FXMLLoader loader = new FXMLLoader(ArticleListController.class.getResource("ArticleList.fxml"));
        ArticleListController controller = new ArticleListController(_dbPath, _password);
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        controller.setMainStage(stage);
        stage.show();

        currentWindow.hide();
    }
}
