package controller_grafici;

import bean.BookingBean;
import bean.ModelBeanFactory;
import controller_applicativi.BookingService;
import javafx.scene.layout.VBox;
import view.BookingViewAlternative;


public class BookingControllerAlternative {

    private final NavigationService      nav;
    private final String                 typeOfLogin;

    private final BookingViewAlternative view    = new BookingViewAlternative();
    private final BookingService         service = new BookingService();

    /* ----------------------------------------------------------- */
    public BookingControllerAlternative(NavigationService nav, String typeOfLogin) {
        this.nav         = nav;
        this.typeOfLogin = typeOfLogin;
        addEventHandlers();
        loadInitialData();
    }

    /* ----------------------------------------------------------- */
    private void addEventHandlers() {
        view.getConfirmButton().setOnAction(_ -> handleConfirm());
        view.getCancelButton() .setOnAction(_ -> nav.navigateToHomePage(nav, typeOfLogin));
    }

    /* ----------------------------------------------------------- */
    private void handleConfirm() {
   
        view.hideAllErrors();
        if (!view.validate()) return;

        BookingBean bean = ModelBeanFactory.getBookingBean(view);

        switch (service.book(bean)) {
            case "success"          -> nav.navigateToHomePage(nav, typeOfLogin);
            case "error:validation" -> view.setGeneralError("Dati non validi o stanza non disponibile");
            default                 -> view.setGeneralError("Errore di sistema, riprova più tardi");
        }
    }

    /* ----------------------------------------------------------- */
    private void loadInitialData() {
        view.loadRooms        (service.getFreeRoomTypes());
        view.loadDestinations (service.getDestinationNames());
        view.loadPackages     (service.getPackageNames());
        view.loadActivities   (service.getActivityNames());
    }

    /* ----------------------------------------------------------- */
    public VBox getRoot() { return view.getRoot(); }
}
