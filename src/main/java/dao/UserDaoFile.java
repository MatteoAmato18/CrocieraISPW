package dao;

import entity.User;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Salva gli utenti in «users.json». Chiave univoca = e-mail. */
public class UserDaoFile implements GenericDao<User> {

    private static final String FILE_PATH = "users.json";
    private final Gson gson;
    private List<User> users;

    public UserDaoFile() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting().create();
        users = loadFromFile();
    }

    /* ------------- I/O ------------- */
    private List<User> loadFromFile() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return new ArrayList<>();

        try (Reader r = new FileReader(f)) {
            Type listType = new TypeToken<List<User>>(){}.getType();
            List<User> loaded = gson.fromJson(r, listType);
            return loaded != null ? loaded : new ArrayList<>();
        } catch (IOException e) { e.printStackTrace(); return new ArrayList<>(); }
    }
    private void save() {
        try (Writer w = new FileWriter(FILE_PATH)) { gson.toJson(users, w); }
        catch (IOException e) { e.printStackTrace(); }
    }

    /* ------------- CRUD ------------- */
    @Override public void create(User u) {
        if (read(u.getEmail()) != null)
            throw new IllegalArgumentException("User già presente: "+u.getEmail());
        users.add(u); save();
    }
    @Override public User read(Object... k) {
        String email = (String) k[0];
        return users.stream().filter(u -> u.getEmail().equals(email))
                              .findFirst().orElse(null);
    }
    @Override public void update(User u) {
        for (int i=0;i<users.size();i++)
            if (users.get(i).getEmail().equals(u.getEmail())) {
                users.set(i,u); save(); return;
            }
        throw new IllegalArgumentException("User non trovato: "+u.getEmail());
    }
    @Override public void delete(Object... k) {
        String email = (String) k[0];
        users.removeIf(u -> u.getEmail().equals(email)); save();
    }
    @Override public List<User> readAll() { return new ArrayList<>(users); }
}