package controller_applicativi;

import bean.ReviewBean;
import dao.DaoFactory;
import dao.GenericDao;
import entity.Activity;
import entity.Review;
import facade.ApplicationFacade;     // <--  nuovo import

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewService {

    private static final Logger LOG =
            Logger.getLogger(ReviewService.class.getName());

    private final GenericDao<Review> reviewDao =
            DaoFactory.getInstance().getReviewDao();

    /* --------------------------------------------------------- */
    /**
     * Salva una recensione e invia mail di conferma.
     *
     * @return "success" | "error:validation" | "error:database_error"
     */
    public String saveReview(ReviewBean bean) {

        /* ---------- 1. Validazione ---------- */
        if (!bean.hasValidStars()   ||
            !bean.hasValidBody()    ||
            !bean.hasValidDateTime()||
            bean.getEmail() == null || bean.getEmail().isBlank())
        {
            return "error:validation";
        }

        /* ---------- 2. Mapping Bean → Entity ---------- */
        Review r = new Review();
        r.setDate          (bean.getDate());
        r.setTime          (bean.getTime());
        r.setEmail         (bean.getEmail());
        r.setStars         (bean.getStars());
        r.setSpecialService(bean.getSpecialService());
        r.setBody          (bean.getBody());

        /* ---------- 3. Persistenza + mail ---------- */
        try {
            reviewDao.create(r);

            /* invio mail di conferma tramite Facade */
            ApplicationFacade.sendReviewConfirmationEmail(r);

            return "success";

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Errore DB durante insert review", ex);
            return "error:database_error";
        }
    }

    /* ----- servizi utili al controller grafico ----- */
    public List<Activity> getAvailableSpecialServices() {
    	GenericDao<Activity> activityDao = DaoFactory.getInstance().getActivityDao();
        return activityDao.readAll();
    }
}