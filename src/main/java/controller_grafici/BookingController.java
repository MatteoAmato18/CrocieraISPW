package controller_grafici;

import bean.BookingBean;
import bean.ModelBeanFactory;
import controller_applicativi.BookingService;
import javafx.scene.layout.VBox;
import view.BookingView;

public class BookingController {

    private final NavigationService nav;
    private final String typeOfLogin;

    private final BookingView view    = new BookingView();
    private final BookingService service = new BookingService();

    public BookingController(NavigationService nav, String typeOfLogin) {
        this.nav = nav; this.typeOfLogin = typeOfLogin;
        addHandlers();
        loadInitialData();
    }

    private void addHandlers() {
        view.getConfirmButton().setOnAction(_ -> handleConfirm());
        view.getCancelButton().setOnAction(_ -> nav.navigateToHomePage(nav, typeOfLogin));
    }

    private void handleConfirm() {
        view.hideAllErrors();
        if (!view.validate()) return;

        BookingBean bean = ModelBeanFactory.getBookingBean(view);
        switch (service.book(bean)) {
            case "success"          -> nav.navigateToHomePage(nav, typeOfLogin);
            case "error:validation" -> view.setGeneralError("Dati non validi o stanza non disponibile");
            default                 -> view.setGeneralError("Errore di sistema, riprova");
        }
    }

    private void loadInitialData() {
        view.loadRooms        (service.getFreeRoomTypes());
        view.loadActivities   (service.getActivityNames());
        view.loadPackages     (service.getPackageNames());
        view.loadDestinations (service.getDestinationNames());
    }

    public VBox getRoot() { return view.getRoot(); }
}
