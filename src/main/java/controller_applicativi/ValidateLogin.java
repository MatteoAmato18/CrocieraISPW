package controller_applicativi;

import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import bean.LoginBean;
import dao.DaoFactory;
import dao.GenericDao;
import entity.Client;
import entity.Staf;
import entity.User;
import exception.WrongLoginCredentialsException;

public class ValidateLogin {

    private static final Logger LOG = Logger.getLogger(ValidateLogin.class.getName());

    private final GenericDao<User> userDao;
    private final GenericDao<Staf> stafDao;
    public ValidateLogin() {
        DaoFactory f = DaoFactory.getInstance();   
        this.userDao = f.getUserDao();
        this.stafDao = f.getStafDao();
    }
    public ValidateLogin(GenericDao<User> userDao,
                         GenericDao<Staf> stafDao) {
        this.userDao = Objects.requireNonNull(userDao);
        this.stafDao = Objects.requireNonNull(stafDao);
    }

    /**
     * @return l'entità autenticata (User o Staf)
     * @throws WrongLoginCredentialsException se credenziali errate o problema DB
     */
    public Client authenticate(LoginBean bean) throws WrongLoginCredentialsException {


if(bean== null || bean.getEmail() == null || bean.getEmail().isBlank()){return null;
}
 if (!bean.hasValidEmail() ||
            !bean.hasValidPassword() ||
            !bean.hasUserType())
{                throw new WrongLoginCredentialsException("Campi mancanti o non validi");
}
        try {
            return switch (bean.getUserType().toLowerCase()) {
                case "user" -> {
                    User u = userDao.read(bean.getEmail());
                    if (match(bean.getPassword(), u.getPassword())) yield u;
                    
                    throw new WrongLoginCredentialsException("Password errata");
                }
                case "staf" -> {
                    Staf s = stafDao.read(bean.getEmail());
                    if (match(bean.getPassword(), s.getPassword())) yield s;
                    throw new WrongLoginCredentialsException("Password errata");
                }
                default -> throw new WrongLoginCredentialsException("Tipo utente sconosciuto");
            };
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Errore DB durante la validazione", ex);
            throw new WrongLoginCredentialsException("Campi non validi");
        }
    }

    
    private boolean match(String raw, String stored) { return raw.equals(stored); }
}