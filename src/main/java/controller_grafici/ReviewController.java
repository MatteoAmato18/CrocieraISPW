package controller_grafici;

import bean.ModelBeanFactory;
import bean.ReviewBean;
import controller_applicativi.ReviewService;
import entity.Activity;
import entity.Client;
import facade.ApplicationFacade;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import view.ReviewView;

import java.util.List;

public class ReviewController {

    private final NavigationService nav;
    private final String            typeOfLogin;

    private final ReviewView     view;
    private final ReviewService  service = new ReviewService();
    private final Client         currentUser;

    public ReviewController(NavigationService nav, String typeOfLogin) {
        this.nav         = nav;
        this.typeOfLogin = typeOfLogin;

        currentUser = ApplicationFacade.getUserFromLogin();

        List<String> names = service.getAvailableSpecialServices()
                                    .stream().map(Activity::getName).toList();
        view = new ReviewView(names);

        view.getSendButton()   .setOnAction(_ -> handleSend());
        view.getCancelButton() .setOnAction(_ -> nav.navigateToHomePage(nav, typeOfLogin));
    }

    private void handleSend() {

        ReviewBean bean = ModelBeanFactory.getReviewBean(view, currentUser);
        if (!bean.hasValidStars())    { err("Seleziona da 1 a 5 stelle"); return; }
        if (!bean.hasValidBody())     { err("Testo troppo corto (min. 250 parole)"); return; }
        if (!bean.hasValidDateTime()) { err("Data/ora non valide"); return; }

        String esito = service.saveReview(bean);
        switch (esito) {
            case "success"          -> nav.navigateToHomePage(nav, typeOfLogin);
            case "error:validation" -> err("Campi non validi");
            default                 -> err("Errore di sistema");
        }
    }

    private void err(String m) {
        Alert a = new Alert(Alert.AlertType.ERROR, m);
        a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

    public VBox getRoot() { return view.getRoot(); }
}