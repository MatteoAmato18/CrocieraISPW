package controller_grafici;



import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

import javafx.scene.layout.VBox;

public class NavigationManager implements NavigationService {
    private String interfaceOption = "color";
    private Stage stage;
    private final Logger logger = Logger.getLogger(NavigationManager.class.getName());

    public NavigationManager(Stage stage) {
        this.stage = stage;
    }



    public void navigateToLogin(NavigationService navigationService,String typeOfLogin) {
        LoginController controller = new LoginController(navigationService,typeOfLogin);
        this.display(controller.getRoot(), "Login");
    }
    public void navigateToRegistration(NavigationService navigationService,  String userType) {
        RegistrationController controller = new RegistrationController(navigationService,  userType);

        this.display(controller.getRoot(), "Registrazione");
    }




    @Override
    public void navigateToHomePage(NavigationService navigationService,String typeOfLogin) {
        HomePageController controller = new HomePageController(navigationService,typeOfLogin);
        this.display(controller.getRoot(), "Home Page");
    }
    public void navigateToBooking(NavigationService navigationService,String typeOfLogin) {
        BookingController controller = new BookingController(navigationService,typeOfLogin);
        this.display(controller.getRoot(), "Booking Page");
    }
   
   
        
        public void navigateToReviview(NavigationService navigationService,String typeOfLogin) {
        	ReviewController controller = new ReviewController(navigationService,typeOfLogin);
            this.display(controller.getRoot(), "Booking Page");
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



    public void setInterfaceOption(String interfaceOption) {
        this.interfaceOption = interfaceOption;
    }

    public String getInterfaceOption() {
        return this.interfaceOption;
    }

    public String getCSSStyle(){
        String cssFile;
        if(this.interfaceOption.equals("color")){
            cssFile = "/style/booking_cruise.css";
        }else{
            cssFile = "/style/bw-mode.css";
        }
        return cssFile;
    }

    public Stage getStage(){
        return stage;
    }
}