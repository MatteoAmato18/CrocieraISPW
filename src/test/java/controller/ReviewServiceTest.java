package controller;

import bean.ReviewBean;
import controller_applicativi.ReviewService;
import dao.DaoFactory;
import dao.GenericDao;
import entity.Activity;
import entity.Review;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReviewServiceTest {

    /* ===== DAO mock ===== */
    @Mock private GenericDao<Review>   reviewDao;
    @Mock private GenericDao<Activity> activityDao;

    /* ===== static-mock ===== */
    private MockedStatic<DaoFactory>        daoFactoryStatic;

    /* ===== SUT ===== */
    private ReviewService service;

    /* ===== dati ===== */
    private ReviewBean validBean;
    private ReviewBean invalidBean;

    /* ----------------------------------------------------------- */
    @BeforeEach
    void setUp()  {

        /* -- 1. static-mock DaoFactory -- */
        daoFactoryStatic = mockStatic(DaoFactory.class);
        DaoFactory f = mock(DaoFactory.class);
        when(f.getReviewDao())  .thenReturn(reviewDao);
        when(f.getActivityDao()).thenReturn(activityDao);
        daoFactoryStatic.when(DaoFactory::getInstance).thenReturn(f);

        
        /* -- 3. SUT -- */
        service = new ReviewService();

        /* -- 4. Bean valido -- */
        validBean = new ReviewBean();
        validBean.setDate (LocalDate.now());
        validBean.setTime (LocalTime.now().withSecond(0).withNano(0));
        validBean.setEmail("alice@example.com");
        validBean.setStars(4);
        validBean.setBody ("Esperienza fantastica! a a a a a a a a a a a a a");
        validBean.setSpecialService("Aperitivo");

        /* -- 5. Bean non valido (manca tutto) -- */
        invalidBean = new ReviewBean();
    }

    @AfterEach
    void tearDown() {
        daoFactoryStatic.close();
    }

    /* ======================= TEST ================================= */

    @Test
    void testReviewValida() throws SQLException {

        String result = service.saveReview(validBean);

        assertEquals("success", result);
        verify(reviewDao).create(any(Review.class));
       
    }

    @Test
    void testReviewNonValida() throws SQLException {

        String result = service.saveReview(invalidBean);

        assertEquals("error:validation", result);
        verify(reviewDao, never()).create(any());
    }

    @Test
    void testErroreDatabase() throws SQLException {

        doThrow(new SQLException("boom")).when(reviewDao).create(any());

        String result = service.saveReview(validBean);

        assertEquals("error:database_error", result);
        verify(reviewDao).create(any());
    }
}
