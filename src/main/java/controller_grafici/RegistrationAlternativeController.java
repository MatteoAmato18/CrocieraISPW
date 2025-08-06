package controller_grafici;


import javafx.scene.layout.VBox;



import bean.ClientRegistrationBean;
import bean.ModelBeanFactory;
import controller_applicativi.ClientRegistrationController;
import view.RegistrationViewAlternative;

public class RegistrationAlternativeController {

    

    private final NavigationService navigationService;
    private final String            userType;          // "user" | "staf" o null

    private final RegistrationViewAlternative view;    // GUI concreta
    private final ClientRegistrationController service = new ClientRegistrationController();

    /* ------------------------------------------------------------ */
    public RegistrationAlternativeController(NavigationService nav, String userType) {
        this.navigationService = nav;
        this.userType          = userType;

        this.view = new RegistrationViewAlternative() {
            
            protected String getTitleText() {
            	return "Registrazione " + title(userType);
            }
             
        };

        addEventHandlers();
       
    }
    private static String title(String type) {
        if (type == null) return "";
        return type.equalsIgnoreCase("user") ? "User" : "Staf";
    }
    /* ------------------------------------------------------------ */
    private void addEventHandlers() {
        view.getConfirmButton().setOnAction(_ -> handleRegistration());
        view.getLoginButton().setOnAction(_  -> navigationService
                .navigateToLogin(navigationService, userType));
    }

    /* ------------------------------------------------------------ */
    private void handleRegistration() {

        /* 1 · popola il bean dalla view */
        ClientRegistrationBean bean =
                ModelBeanFactory.getClientRegistrationBean(view);

        if (userType != null) bean.setUserType(userType);   // scelta fissa
        /* altrimenti l’ha già impostato il factory via getSelectedUserType() */

        /* 2 · validazione GUI usando i metodi atomici del bean */
        boolean ok = true;
        view.hideAllErrors();

        if (!bean.hasValidFirstName())  { view.showFirstNameError();                     ok = false; }
        if (!bean.hasValidLastName())   { view.showLastNameError();                      ok = false; }
        if (!bean.hasValidEmail())      { view.showEmailError("email non conforme");      ok = false; }
        if (!bean.hasValidPhone())      { view.showPhoneNumberError("10 cifre richieste"); ok = false; }
        if (!bean.hasValidPassword())   { view.showPasswordError("8-16 caratteri");      ok = false; }
        if (!bean.passwordsMatch())     { view.showRepeatPasswordError("Le password non coincidono"); ok = false; }

        if (!ok) return;                // fermati – errori visibili in GUI

        /* 3 · business: tenta la registrazione */
        String esito = service.registerUser(bean);

        switch (esito) {
            case "success" -> navigateToNextPage();

            case "error:user_already_exists" ->
                view.showEmailError("Email o telefono già registrati");

            case "error:database_error" ->
                view.getDatabaseError().setText("Errore di sistema. Riprova più tardi.");

            case "error:validation" ->
                view.getDatabaseError().setText("Dati non validi.");

            default ->
                view.getDatabaseError().setText("Errore sconosciuto.");
        }
    }

    /* ------------------------------------------------------------ */
    private void navigateToNextPage() {
        navigationService.navigateToHomePage(navigationService,
                                             userType != null ? userType : "user");
    }

    /* ------------------------------------------------------------ */
    public VBox getRoot() { return view.getRoot(); }
}