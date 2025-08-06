package facade;



import javafx.scene.control.Alert;

import java.util.logging.Logger;

import bean.LoginBean;
import bean.ModelBeanFactory;
import controller_applicativi.ValidateLogin;
import dao.DaoFactory;
import dao.DaoFactory.Store;
import entity.Booking;
import entity.Client;
import entity.Review;
import exception.WrongLoginCredentialsException;

public final class ApplicationFacade {

    /* ------------------------------------------------------------ */
    private static final Logger LOG = Logger.getLogger(ApplicationFacade.class.getName());

    // inizializzata da init()
    private static ValidateLogin validator;  // idem

    private ApplicationFacade() {}           // utility-class: no istanze


    /* ============================================================ */
    public static void init(Store storageMode) {


        DaoFactory.getInstance();
        DaoFactory.setStorageOption(storageMode);

        validator = new ValidateLogin();

        LOG.info(() -> "ApplicationFacade inizializzata in modalità " + storageMode);
    }



    /** Controlla se le credenziali sono valide. */
    public static boolean isLoginValid(LoginBean bean) {
        ensureInit();
        try {
            return validator.authenticate(bean) != null;
        } catch (WrongLoginCredentialsException _) {
            return false;
        }
    }

    /** Ritorna l’oggetto Client della sessione corrente, oppure null. */
    public static Client getUserFromLogin() {
        ensureInit();
        try {
            return validator.authenticate(ModelBeanFactory.loadLoginBean());
        } catch (WrongLoginCredentialsException _) {
            return null;
        }
    }

    /** Ritorna "user", "staf", … se l’utente in sessione è ancora valido. */
    public static String checkLoginStatus() {
        ensureInit();
        try {
            LoginBean bean = ModelBeanFactory.loadLoginBean();
            if (bean == null) return null;
            if (validator.authenticate(bean) == null) return null;
            return bean.getUserType();
        } catch (WrongLoginCredentialsException _) {
            LOG.info("Credenziali non valide in sessione"); return null;
        }
    }
    
    public static void sendReviewConfirmationEmail(Review r) {

        String to = r.getEmail();
        String subject = "Grazie per la tua recensione!";
        String body = """
                      Ciao,

                      abbiamo ricevuto la tua recensione del %s alle %s.

                      Valutazione: %d stelle
                      Servizio speciale: %s

                      Testo della recensione:
                      %s

                      A presto! 
                      """
                .formatted(r.getDate(), r.getTime(),
                           r.getStars(), 
                           r.getSpecialService() == null ? "-" : r.getSpecialService(),
                           r.getBody());

        EmailService.sendEmail(to, subject, body);
    }
    public static void sendBookingReceivedEmail(Booking b) {

        String subject = "Richiesta prenotazione ricevuta";
        String body = """
                      Ciao %s %s,

                      abbiamo ricevuto la tua richiesta di prenotazione, ecco i dettagli:

                      • Destinazione   : %s
                      • Pacchetto      : %s
                      • Tipo di stanza : %s
                      • Attività       : %s
                      • Telefono       : %s

                      Il nostro staff la controllerà al più presto.
                      Riceverai una seconda e‑mail di conferma non appena la prenotazione
                      sarà approvata.

                      A presto!
                      """
                .formatted(b.getFirstName(), b.getLastName(),
                           b.getDestinationName(),
                           b.getPackageName(),
                           b.getRoomType(),
                           b.getActivityName(),
                           b.getPhoneNumber());

        EmailService.sendEmail(b.getEmail(), subject, body);
    }

    /* ------------------------------------------------------------- */
    /** Mail di conferma definitiva (stato CONFIRMED / OCCUPATED). */
    public static void sendBookingConfirmationEmail(Booking b) {

        String subject = "Prenotazione confermata! #" + b.getId();
        String body = """
                      Ciao %s %s,

                      la tua prenotazione è stata confermata 

                      • ID             : %d
                      • Destinazione   : %s
                      • Pacchetto      : %s
                      • Stanza         : %s
                      • Attività       : %s

                      Ti aspettiamo a bordo!
                      """
                .formatted(b.getFirstName(), b.getLastName(),
                           b.getId(),
                           b.getDestinationName(),
                           b.getPackageName(),
                           b.getRoomType(),
                           b.getActivityName());

        EmailService.sendEmail(b.getEmail(), subject, body);
    }

    /* ------------------------------------------------------------- */
    public static void sendBookingCancelledEmail(Booking b) {

        String subject = "Prenotazione annullata – ID " + b.getId();
        String body = """
                      Ciao %s %s,

                      la tua prenotazione (ID %d) è stata annullata.

                      Se hai domande o desideri prenotare di nuovo,
                      non esitare a contattarci.

                      A presto!
                      """
                .formatted(b.getFirstName(), b.getLastName(), b.getId());

        EmailService.sendEmail(b.getEmail(), subject, body);
    }

   
    /* ===================  Helper grafici  ======================= */

    public static void showErrorMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /* =======================  private debug  ========================== */

    private static void ensureInit() {
        if (validator == null)
            throw new IllegalStateException("ApplicationFacade.init(...) non è stato chiamato!");
    }
}