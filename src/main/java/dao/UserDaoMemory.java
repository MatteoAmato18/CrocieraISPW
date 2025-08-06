package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import exception.UserAlreadyInsertedException;
import exception.WrongLoginCredentialsException;

public class UserDaoMemory implements GenericDao<User> {
    private final ObservableList<User> storage;
    private static final String USER_CANNOT_BE_NULL = "User cannot be null";

    public UserDaoMemory() {
        storage = FXCollections.observableArrayList();
        storage.add(new User("User", "Test", "user@cafe.com", "56465465",  "admin123"));

    }
    
    @Override
    public void create(User user) throws SQLException {
        if (user == null) throw new SQLException(USER_CANNOT_BE_NULL);
        if (!isEmailUnique(user.getEmail())) {
            throw new UserAlreadyInsertedException("Esiste già un utente con questa email");
        }
        if (!isPhoneNumberUnique(user.getPhoneNumber())) {
            throw new UserAlreadyInsertedException("Esiste già un utente questo numero di telefono");
        }
        storage.add(user);
    }

    @Override
    public User read(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Chiave non valida per la ricerca del utente");
        }

        String email = (String) keys[0];
        return storage.stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new WrongLoginCredentialsException("Credenziali non corrette"));
    }

    @Override
    public void update(User user) throws SQLException {
        if (user == null) throw new SQLException(USER_CANNOT_BE_NULL);
        User existingUser = read(user.getEmail()); // Verifica se esiste già
        storage.remove(existingUser); // Rimuovi il vecchio record
        storage.add(user); // Aggiungi il record aggiornato
    }

    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Chiave non valida per l'eliminazione del utente");
        }

        String email = (String) keys[0];
        User existingUser = read(email);
        storage.remove(existingUser);
    }

    private boolean isEmailUnique(String email) {
        return storage.stream().noneMatch(user -> user.getEmail().equals(email));
    }

    private boolean isPhoneNumberUnique(String phoneNumber) {
        return storage.stream().noneMatch(user -> user.getPhoneNumber().equals(phoneNumber));
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(storage);
    }
}