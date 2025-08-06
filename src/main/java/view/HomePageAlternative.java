package view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class HomePageAlternative {
    protected VBox root;
    private final RadioButton bookSeatOption;
    private final RadioButton reviviewOption;
    
    private final RadioButton activityOption;
    private final Button confirmButton;
    private final ToggleGroup selectionGroup;
    private final Label selectionError;
    private final RadioButton changeToStaftOption;
    private final RadioButton changeToUserOption;
	private String typeOfLogin;
	  String role;
    

    public HomePageAlternative(String typeOfLogin) {
        // Inizializzazione layout
    	this.typeOfLogin=typeOfLogin;
    	role=typeOfLogin;
    	
		boolean isUser = "user".equalsIgnoreCase(role);
        boolean isStaf = "staf".equalsIgnoreCase(role);
        root = new VBox(20);
        root.setPrefSize(1280, 720);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        root.setAlignment(Pos.CENTER);

        // Titolo e descrizione
        Label title = new Label("Benvenuti nel nostro CATCAFE'!");
        

        // Gruppo di selezione con RadioButton
        selectionGroup = new ToggleGroup();

        bookSeatOption = new RadioButton("Prenota la tua stanza per un viaggio da sogno");
        
        reviviewOption= new RadioButton("Lascia una tua recensione");
        activityOption = new RadioButton("Gestisci le attività");
        changeToStaftOption= new RadioButton("Vuoi passare al pannello di controllo dello Staf?");
        changeToUserOption= new RadioButton("Vuoi passare alla schermata da Utente?");
        
        bookSeatOption.setToggleGroup(selectionGroup);
        reviviewOption.setToggleGroup(selectionGroup);
        activityOption.setToggleGroup(selectionGroup);
        changeToStaftOption.setToggleGroup(selectionGroup);
        changeToUserOption.setToggleGroup(selectionGroup);
        
        bookSeatOption .setDisable(!isUser);
        changeToStaftOption.setDisable(!isUser);
        reviviewOption.setDisable(!isUser);

        activityOption .setDisable(!isStaf);
        changeToUserOption.setDisable(!isStaf);
        // Messaggio di errore nascosto inizialmente
        selectionError = new Label("Seleziona un'opzione prima di confermare.");
        selectionError.setStyle("-fx-text-fill: red;");
        selectionError.setVisible(false);
        
        // Bottone di conferma
        confirmButton = new Button("Conferma Scelta");
        

        // Aggiunta degli elementi alla root
        root.getChildren().addAll(
                title,
               
                bookSeatOption,
               
                reviviewOption,
                
                activityOption,
                selectionError,
                confirmButton,
                changeToStaftOption,
                changeToUserOption
        );
    }

    public RadioButton getBookSeatOption() {
        return bookSeatOption;
    }

    
    public RadioButton getReviviewOption() {
        return reviviewOption;
    }

    
    public RadioButton getCatOption() {
        return activityOption;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }
    public RadioButton getChangeToStafButton() {
    	return changeToStaftOption; 
    	}
    public RadioButton getChangeToUserButton() {
    	return changeToUserOption; 
    	}
    

    public ToggleGroup getSelectionGroup() {
        return selectionGroup;
    }

    public void showSelectionError() {
        selectionError.setVisible(true);
    }

    public VBox getRoot() {
        return root;
    }
}