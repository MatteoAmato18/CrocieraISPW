package controller_applicativi;



import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import bean.ClientRegistrationBean;
import dao.DaoFactory;
import dao.GenericDao;
import dao.SessionManager;
import entity.Client;
import entity.Staf;
import entity.User;
import exception.UserAlreadyInsertedException;

public class ClientRegistrationController {

    private static final Logger LOG =
            Logger.getLogger(ClientRegistrationController.class.getName());

    /* DAO ottenuti dall’unica factory configurata in ApplicationFacade.init() */
    private final GenericDao<User> userDao;
    private final GenericDao<Staf> stafDao;

    public ClientRegistrationController() {
        DaoFactory f = DaoFactory.getInstance();          // singleton
        this.userDao = f.getUserDao();
        this.stafDao = f.getStafDao();
    }

    /*==================================================================*/
    /** Registra l’utente e ritorna "success" oppure un codice d’errore. */
    public String registerUser(ClientRegistrationBean bean) {

        /* -------- validazione di business minima -------- */
        if (bean == null ||
            !bean.hasValidEmail() ||
            !bean.hasValidPassword() ||
            !bean.passwordsMatch()   ||
            !bean.hasUserType()) {
            return "error:validation";
        }

        /* -------- mapping bean → entity -------- */
        Client client = mapBeanToEntity(bean);
        if (client == null) return "error:unknown_user_type";

        /* -------- persistenza -------- */
        try {
            if (client instanceof User u) {
                userDao.create(u);
            } else if (client instanceof Staf s) {
                stafDao.create(s);
            }

            /* -------- sessione e log -------- */
            SessionManager.getInstance()
                    .setCredentials(client.getEmail(), client.getPassword(), client.getUserType());

            LOG.info(client.getUserType() + " registrato: " + client.getEmail());
            return "success";

        } catch (UserAlreadyInsertedException e) {
            LOG.warning(e.getMessage());
            return "error:user_already_exists";

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState()))          // vincolo UNIQUE
                return "error:user_already_exists";

            LOG.log(Level.SEVERE, "Errore DB durante la registrazione", e);
            return "error:database_error";
        }
    }

    /*==================================================================*/
    /** Converte il bean nell’entità corretta (User o Staf). */
    private Client mapBeanToEntity(ClientRegistrationBean b) {

        if ("user".equalsIgnoreCase(b.getUserType())) {
            User u = new User();
            u.setFirstName(b.getFirstName());
            u.setLastName(b.getLastName());
            u.setEmail(b.getEmail());
            u.setPhoneNumber(b.getPhoneNumber());
            u.setPassword(hash(b.getPassword()));         // hash in produzione
            return u;

        } else if ("staf".equalsIgnoreCase(b.getUserType())) {
            Staf s = new Staf();
            s.setFirstName(b.getFirstName());
            s.setLastName(b.getLastName());
            s.setEmail(b.getEmail());
            s.setPassword(hash(b.getPassword()));
            // s.setPhoneNumber(b.getPhoneNumber());      // se serve anche per lo staff
            return s;
        }
        return null;                                      // tipo sconosciuto
    }

    /* === placeholder: sostituisci con BCrypt, Argon2, ecc. ============ */
    private String hash(String rawPwd) {
        return rawPwd;   // **NON** usare in produzione! Solo demo.
    }
}