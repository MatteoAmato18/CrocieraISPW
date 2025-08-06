package view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public  class LoginAlternativeView {
    private VBox root;
    private Label titleLabel;
    private TextField emailField;
    private Label emailErrorLabel;
    private PasswordField passwordField;
    private Label passwordErrorLabel;
    private RadioButton clientLoginOption;
    private RadioButton stafLoginOption;
    private ToggleGroup loginTypeGroup;
    private Button confirmButton;
   
    private Text errorMessage;
    private Text registrationPrompt;
    private Button registerButton;

    public LoginAlternativeView() {
        root = new VBox(15);
        root.setPrefSize(1280, 720);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        titleLabel = new Label("Benventuto accedi con le tue credenziali");
        

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailErrorLabel = new Label("Inserisci un'email valida");
        emailErrorLabel.setVisible(false);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordErrorLabel = new Label("La password non può essere vuota");
        passwordErrorLabel.setVisible(false);

        // Gruppo di selezione del tipo di login
        loginTypeGroup = new ToggleGroup();
        clientLoginOption = new RadioButton("User");
        stafLoginOption = new RadioButton("Staf");
        clientLoginOption.setToggleGroup(loginTypeGroup);
        stafLoginOption.setToggleGroup(loginTypeGroup);

        this.errorMessage = new Text("Email o password non corrispondono a un account");
        this.errorMessage.setVisible(false);
        this.errorMessage.setManaged(false);

        confirmButton = new Button("Conferma");
        

        registrationPrompt = new Text("Se non ti sei mai prenotato, puoi registrarti.");
        registrationPrompt.getStyleClass().add("registration-prompt");

        registerButton = new Button("Registrati");

        root.getChildren().addAll(titleLabel, emailField, emailErrorLabel, passwordField, passwordErrorLabel, clientLoginOption, stafLoginOption, errorMessage, confirmButton,  registrationPrompt, registerButton);
    }

   

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

    public RadioButton getClientLoginOption() {
        return clientLoginOption;
    }

    public RadioButton getReceptionistLoginOption() {
        return stafLoginOption;
    }

    public ToggleGroup getLoginTypeGroup() {
        return loginTypeGroup;
    }

    public Button getConfirmButton() {
        return confirmButton;
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
    public void hideUserTypeToggle() {
        root.getChildren().remove(clientLoginOption);
        root.getChildren().remove(stafLoginOption);
    }
}