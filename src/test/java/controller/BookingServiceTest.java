package controller;

import bean.BookingBean;
import controller_applicativi.BookingService;
import dao.DaoFactory;
import dao.GenericDao;
import entity.*;
import facade.ApplicationFacade;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test sviluppatato da Matteo Amato
 * Test d’integrazione per BookingService.
 * Tutti i DAO e l’invio di e‑mail sono mock.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)   //  ← soluzione A
class BookingServiceTest {

    /* ========= DAO mock ========= */
    @Mock private GenericDao<Booking>       bookingDao;
    @Mock private GenericDao<Room>          roomDao;
    @Mock private GenericDao<Activity>      activityDao;
    @Mock private GenericDao<CruisePackage> packageDao;
    @Mock private GenericDao<Destination>   destinationDao;

    /* ========= static‑mock ========= */
    private MockedStatic<DaoFactory>        daoFactoryStatic;
    private MockedStatic<ApplicationFacade> facadeStatic;

    /* ========= SUT ========= */
    private BookingService service;

    /* ========= dati di prova ========= */
    private BookingBean validBean;
    private BookingBean invalidBean;

    /* --------------------------------------------------------------- */
    @BeforeEach
    void setUp() throws SQLException {

        /* ---- 1. static‑mock di DaoFactory ---- */
        daoFactoryStatic = mockStatic(DaoFactory.class);
        DaoFactory factory = mock(DaoFactory.class);

        when(factory.getBookingDao())   .thenReturn(bookingDao);
        when(factory.getRoomDao())      .thenReturn(roomDao);
        // questi tre stubs non servono a book(), ma ora non daranno errori
        when(factory.getActivityDao())      .thenReturn(activityDao);
        when(factory.getCruisePackageDao()) .thenReturn(packageDao);
        when(factory.getDestinationDao())   .thenReturn(destinationDao);

        daoFactoryStatic.when(DaoFactory::getInstance).thenReturn(factory);

        /* ---- 2. static‑mock dell’e‑mail ---- */
        facadeStatic = mockStatic(ApplicationFacade.class);
        facadeStatic.when(() -> ApplicationFacade.sendBookingReceivedEmail(any()))
                    .then(inv -> null);   // disattiva l’invio reale

        /* ---- 3. Stanza mock FREE ---- */
        Room freeRoom = new Room("Suite", 1500, "Suite con balcone", 2, RoomStatus.FREE);
        when(roomDao.read(any())).thenReturn(freeRoom);

        /* ---- 4. Istanzia SUT ---- */
        service = new BookingService();

        /* ---- 5. Bean valido ---- */
        validBean = new BookingBean();
        validBean.setFirstName      ("Alice");
        validBean.setLastName       ("Wonderland");
        validBean.setEmail          ("alice@example.com");
        validBean.setPhoneNumber    ("0123456789");
        validBean.setRoomType       ("Suite");
        validBean.setDestinationName("Caraibi");
        validBean.setPackageName    ("All Inclusive");
        validBean.setActivityName   ("Snorkeling");

        /* ---- 6. Bean non valido ---- */
        invalidBean = new BookingBean(); // manca tutto => validation fail
    }

    @AfterEach
    void tearDown() {
        daoFactoryStatic.close();
        facadeStatic.close();
    }

    /* =============================================================== */

    @Test
    void testPrenotazioneValida() throws SQLException {

        String result = service.book(validBean);

        assertEquals("success", result);
        verify(bookingDao).create(any(Booking.class));
        verify(roomDao)   .update(any(Room.class)); // stanza->PENDING
    }

    @Test
    void testPrenotazioneNonValida() throws SQLException {

        String result = service.book(invalidBean);

        assertEquals("error:validation", result);
        verify(bookingDao, never()).create(any());
    }
}
