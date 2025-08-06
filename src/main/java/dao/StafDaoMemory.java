package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Staf;
import exception.UserAlreadyInsertedException;
import exception.WrongLoginCredentialsException;

public class StafDaoMemory implements GenericDao<Staf> {
    private final List<Staf> storage = new ArrayList<>();

    public StafDaoMemory() {
        // Aggiunta di Staf predefiniti
        storage.add(new Staf("Admin", "User", "test@gmail.com",   "1"));
        
    }

    @Override
    public void create(Staf staf) {
        if (staf == null) throw new IllegalArgumentException("Staf non può essere null");
        if (!isEmailUnique(staf.getEmail())) {
            throw new UserAlreadyInsertedException("Esiste già uno staf con questa email");
        }
        storage.add(staf);
    }

    @Override
    public Staf read(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Devi fornire un solo parametro di tipo String (email).");
        }
        String email = (String) keys[0];

        return storage.stream()
                .filter(staf -> staf.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new WrongLoginCredentialsException("Credenziali non corrette"));
    }

    @Override
    public void update(Staf staf) throws SQLException {
        if (staf == null) throw new SQLException("Staf cannot be null");
        Staf existingReceptionist = read(staf.getEmail()); // Verifica se esiste già
        storage.remove(existingReceptionist); // Rimuovi il vecchio record
        storage.add(staf); // Aggiungi il record aggiornato
    }

    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Devi fornire un solo parametro di tipo String (email).");
        }
        String email = (String) keys[0];

        Staf existingStaf = read(email); // Verifica se esiste già
        storage.remove(existingStaf);
    }

    private boolean isEmailUnique(String email) {
        return storage.stream().noneMatch(staf -> staf.getEmail().equals(email));
    }

    @Override
    public List<Staf> readAll() {
        return new ArrayList<>(storage); // Restituisce una copia della lista
    }
}