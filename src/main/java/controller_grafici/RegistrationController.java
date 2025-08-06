package controller_grafici;

import javafx.scene.layout.VBox;



import bean.ClientRegistrationBean;
import bean.ModelBeanFactory;
import controller_applicativi.ClientRegistrationController;
import dao.SessionManager;

import view.RegistrationView;

public class RegistrationController {

   

    /* ------------------------------------------------------------ */
    private final NavigationService navigationService;
    private final String            fixedUserType;      // "user" | "staf" | null
    private final RegistrationView view;
    private final ClientRegistrationController service =
            new ClientRegistrationController();

    /* ------------------------------------------------------------ */
    public RegistrationController(NavigationService nav, String userType) {
        this.navigationService = nav;
        this.fixedUserType     = userType;

        this.view = new RegistrationView(userType);   // tua view concreta
        addEventHandlers();
    }

    /* ------------------------------------------------------------ */
    private void addEventHandlers() {
        view.getRegisterButton().setOnAction(_ -> handleRegistration());
        view.getLoginButton().setOnAction(_     -> navigationService
                .navigateToLogin(navigationService, fixedUserType));
        
        
    }

    /* ------------------------------------------------------------ */
    private void handleRegistration() {

        /* 1 · popola il bean */
        ClientRegistrationBean bean =
                ModelBeanFactory.getClientRegistrationBean(view);

        if (fixedUserType != null) bean.setUserType(fixedUserType);

        /* 2 · validazione GUI tramite bean */
        boolean ok = true;
        view.hideAllErrors();

        if (!bean.hasValidFirstName())   { view.showFirstNameError();                    
        ok = false; }
        if (!bean.hasValidLastName())    { view.showLastNameError();                   
        ok = false; }
        if (!bean.hasValidEmail())       { view.showEmailError("email non conforme");             
        ok = false; }
        if (!bean.hasValidPhone())       { view.showPhoneNumberError("10 cifre richieste"); 
        ok = false; }
        if (!bean.hasValidPassword())    { view.showPasswordError("8-16 caratteri");     
        ok = false; }
        if (!bean.passwordsMatch())      { view.showRepeatPasswordError("Le password non coincidono");
        ok = false; }

        if (!ok) return;

        /* 3 · business: tenta la registrazione */
        String esito = service.registerUser(bean);

        switch (esito) {
            case "success" -> {
                /* eventuale login automatico dopo la registrazione */
                SessionManager.getInstance()
                        .setCredentials(bean.getEmail(),
                                         bean.getPassword(),
                                         bean.getUserType());
                navigationService.navigateToHomePage(navigationService,
                        bean.getUserType());
            }

            case "error:user_already_exists" ->
                view.showEmailError("Email o telefono già registrati");

            case "error:database_error" ->
                view.showDatabaseError("Errore di sistema. Riprova più tardi.");

            

            default ->
                view.showDatabaseError("Errore sconosciuto.");
        }
    }

    /* ------------------------------------------------------------ */
    public VBox getRoot() { return view.getRoot(); }
}