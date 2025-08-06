package controller_grafici;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;

import javafx.scene.layout.VBox;


public class NavigationManagerAlternative implements NavigationService {
    private String interfaceOption = "bw";
    private Stage stage;
    private final Logger logger = Logger.getLogger(NavigationManagerAlternative.class.getName());

    public NavigationManagerAlternative(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }

    public void setInterfaceOption(String interfaceOption) {
        this.interfaceOption = interfaceOption;
    }

    public String getCSSStyle() {
        String cssFile;
        if (this.interfaceOption.equals("color")) {
            cssFile = "/style/color-mode.css";
        } else {
            cssFile = "/style/bw-mode.css";
        }
        return cssFile;
    }

    

    public void display(VBox root, String title) {
        try {
            Scene scene = new Scene(root);
            scene.getStylesheets().clear();
            String styleCSS = getCSSStyle();
            scene.getStylesheets().add(getClass().getResource(styleCSS).toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            logger.warning("Errore durante il caricamento della vista: " + e.getMessage());
        }
    }

   
   
   @Override
    public void navigateToHomePage(NavigationService navigationService,String typeOfLogin) {
        HomePageAlternativeController controller = new HomePageAlternativeController(navigationService, typeOfLogin);
        this.display(controller.getRoot(), "Home Page (Alternativa)");
    }

  

    public void navigateToLogin(NavigationService navigationService,String typeOfLogin){
        LoginAlternativeController controller = new LoginAlternativeController(navigationService,  typeOfLogin);
        this.display(controller.getRoot(), "Login Alternativo");
    }
    public void navigateToBooking(NavigationService navigationService,String typeOfLogin) {
    	BookingControllerAlternative controller = new BookingControllerAlternative(navigationService,typeOfLogin);
        this.display(controller.getRoot(), "Booking Page");
    }
   
    
   
  public void navigateToRegistration(NavigationService navigationService, String typeOfLogin) {
        RegistrationAlternativeController controller = new RegistrationAlternativeController(navigationService, typeOfLogin);
        this.display(controller.getRoot(), "Registrazione (secondaria)");
    }
  public void navigateToReviview(NavigationService navigationService, String typeOfLogin) {
	  ReviewAltController controller = new ReviewAltController(navigationService, typeOfLogin);
      this.display(controller.getRoot(), "Registrazione (secondaria)");
  }
}