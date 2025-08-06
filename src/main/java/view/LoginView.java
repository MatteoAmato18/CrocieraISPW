package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public  class LoginView {
    private VBox root;
    private Label titleLabel;
    private TextField emailField;
    private Label emailErrorLabel;
    private PasswordField passwordField;
    private Label passwordErrorLabel;
    private Button loginButton;
    
    private Text errorMessage;
    private Text registrationPrompt;
    private Button registerButton;
	private String typeOfLogin;
	

    public LoginView(String typeOfLogin) {
    	this.typeOfLogin= typeOfLogin;
        root = new VBox(15);
        root.setPrefSize(1280, 720);
        root.getStyleClass().add("root");
        //risolve code smell
        String button ="button";
        if(typeOfLogin.equals("user")) {

        titleLabel = new Label("Benventuto accedi con le tue credenziali");
        }
        else {
        titleLabel = new Label("Stai accedendo come membro del nostro staff, inserisci le tue credenziali");}

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailErrorLabel = new Label("Inserisci un'email valida");
        emailErrorLabel.setVisible(false);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordErrorLabel = new Label("La password non può essere vuota");
        passwordErrorLabel.setVisible(false);

        this.errorMessage = new Text("Email o password non corrispondono a un account");
        this.errorMessage.setVisible(false);
        this.errorMessage.setManaged(false);

        loginButton = new Button("Conferma");
        loginButton.getStyleClass().add(button);

       
        registrationPrompt = new Text("Se non ti sei mai prenotato, puoi registrarti.");
        registrationPrompt.getStyleClass().add("registration-prompt");

        registerButton = new Button("Registrati");
        registerButton.getStyleClass().add(button);
        
        

        root.getChildren().addAll(titleLabel, emailField, emailErrorLabel, passwordField, passwordErrorLabel, errorMessage, loginButton,  registrationPrompt, registerButton);
    }

    
    public  String getType(){
    return typeOfLogin;}

    public void hideErrorMessages() {
        emailErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        errorMessage.setVisible(false);
    }

    public void showEmailError() {
        emailErrorLabel.setVisible(true);
    }

    public void showPasswordError() {
        passwordErrorLabel.setVisible(true);
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    

    public VBox getRoot() {
        return root;
    }

    public Text getErrorMessage() {
        return errorMessage;
    }

    public Text getRegistrationPrompt() {
        return registrationPrompt;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}