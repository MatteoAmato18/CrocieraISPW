package dao;


import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import dao.DaoFactory.Store;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test sviluppatato da Matteo Amato
 * Test di integrazione su { DaoFactory}.
 */
class DaoFactoryTest {

    /* ==============================================================
                           MODALITÀ  MEMORY
       ============================================================== */
    @Test
    void givenStateless_whenGetUserDao() {
        DaoFactory.setStorageOption(Store.STATELESS);

        assertInstanceOf(UserDaoMemory.class,
                         DaoFactory.getInstance().getUserDao());
    }

    @Test
    void givenStateless_whenGetStafDao() {
        DaoFactory.setStorageOption(Store.STATELESS);

        assertInstanceOf(StafDaoMemory.class,
                         DaoFactory.getInstance().getStafDao());
    }

    @Test
    void givenStateless_whenGetBookingDao() {
        DaoFactory.setStorageOption(Store.STATELESS);

        assertInstanceOf(BookingDaoMemory.class,
                         DaoFactory.getInstance().getBookingDao());
    }

    /* ==============================================================
                           MODALITÀ  DATABASE
       ============================================================== */
    @Test
    void givenDatabase_whenGetDaos_thenUseConnectionManager() {

        /* 1.  mock statico di DatabaseConnectionManager.getConnection() */
        try (MockedStatic<DatabaseConnectionManager> m =
                     mockStatic(DatabaseConnectionManager.class)) {

            Connection fakeConn = mock(Connection.class);
            m.when(DatabaseConnectionManager::getConnection)
             .thenReturn(fakeConn);

            /* 2.  set factory in modalità DB */
            DaoFactory.setStorageOption(Store.DATABASE);

            /* 3.  tutti i DAO restituiti DEVONO essere le versioni DB   */
            assertInstanceOf(UserDaoDB.class,
                             DaoFactory.getInstance().getUserDao());
            assertInstanceOf(StafDaoDB.class,
                             DaoFactory.getInstance().getStafDao());
            assertInstanceOf(BookingDaoDB.class,
                             DaoFactory.getInstance().getBookingDao());

            /* 4.  verifica che la connessione sia stata richiesta       */
            m.verify(DatabaseConnectionManager::getConnection,
                     atLeastOnce());
        }
    }
}