package dao;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import exception.DatabaseConnectionException;

public class DatabaseConnectionManager {
    private static final Logger logger = Logger.getLogger(DatabaseConnectionManager.class.getName());
    private static Connection connection;

    private static String url;
    private static String user;
    private static String password;

    private DatabaseConnectionManager() {
        throw new IllegalStateException("Utility class");
    }

    static {
        // Carica i dati di configurazione
    	try (InputStream in = DatabaseConnectionManager.class
    	        .getResourceAsStream("/dbconfig.properties")) {   // <-- slash iniziale
    	    if (in == null) throw new IllegalStateException("dbconfig.properties non trovato!");

    	    Properties p = new Properties();
    	    p.load(in);

    	    url      = p.getProperty("db.url");
    	    user     = p.getProperty("db.user");
    	    password = p.getProperty("db.password");

    	    
    	}  catch (IOException _) {
    		logger.info("Attenzione: impossibile caricare la configurazione del database, usando valori di default.");
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                logger.info("Connessione al database ristabilita.");
            }
        } catch (SQLException e) {
        	e.printStackTrace();    
            throw new DatabaseConnectionException("Impossibile connettersi al database", e);
        }
        return connection;
    }


    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                logger.info("Connessione al database chiusa.");
            } catch (SQLException e) {
                logger.warning("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }
}