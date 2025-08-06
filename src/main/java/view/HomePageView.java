package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomePageView {

    private final VBox    root = new VBox(20);

    private final Button bookSeatButton     = new Button("Prenota la tua stanza per un viaggio da sogno");
    
   
    private final Button menageActivityButton    = new Button("Gestisci Le attività");
    private final Button logoutButton       = new Button("Logout");
    private final Button reviviewButton       = new Button("Lascia una tua recensione");
    

    /* ------------------------------------------------------------ */
    public HomePageView(String typeOfLogin) {

        root.setPrefSize(1280, 720);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        boolean isUser = "user".equalsIgnoreCase(typeOfLogin);
        boolean isStaf = "staf".equalsIgnoreCase(typeOfLogin);

        /* Titolo */
        Label title = new Label("Benvenuti nel luogo dove prenotare la tua vacanza da sogno");

        /* Messaggio descrittivo */
        String msg = isUser
                ? "Benvenuti nel luogo dove prenotare la tua vacanza da sogno"
                : "Benvenuto nel pannello di controllo per lo staf.";
        Label description = new Label(msg);

        /* Abilita/disabilita bottoni in base al ruolo */
        bookSeatButton.setDisable(!isUser);
        reviviewButton.setDisable(!isUser);

        menageActivityButton.setDisable(!isStaf);

        /* Ordine nella VBox: titolo → descrizione → bottoni */
        root.getChildren().addAll(
                title, description,
                bookSeatButton,reviviewButton,
                menageActivityButton,
                logoutButton
        );
    }

   

    public VBox   getRoot()  {
    	return root; 
    	}
    public Button getBookButton()  {
    	return bookSeatButton; 
    	}
    
    
    public Button getManageActivity() {
    	return menageActivityButton; 
    	}
    public Button getReviviewButton() {
    	return reviviewButton; 
    	}

    public Button getLogoutButton() {
    	return logoutButton; 
    	}
}