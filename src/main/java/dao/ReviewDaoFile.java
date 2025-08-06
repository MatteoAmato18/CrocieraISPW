package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Review;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** DAO file-based (JSON) per Review. */
public class ReviewDaoFile implements GenericDao<Review> {

    private static final String FILE_PATH = "reviews.json";
    private final Gson gson;
    private List<Review> reviews;

    public ReviewDaoFile() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .setPrettyPrinting()
                .create();
        reviews = loadFromFile();
    }

    /* ---------------- CRUD ---------------- */
    @Override
    public void create(Review r) {
        r.setId(nextId());
        reviews.add(r);
        save();
    }

    @Override
    public Review read(Object... keys) {
        int id = keyToId(keys);
        return reviews.stream()
                      .filter(r -> r.getId() == id)
                      .findFirst().orElse(null);
    }

    @Override
    public void update(Review r) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getId() == r.getId()) {
                reviews.set(i, r);
                save();
                return;
            }
        }
        throw new IllegalArgumentException("Review id not found: " + r.getId());
    }

    @Override
    public void delete(Object... keys) {
        int id = keyToId(keys);
        reviews.removeIf(r -> r.getId() == id);
        save();
    }

    @Override
    public List<Review> readAll() {
        return new ArrayList<>(reviews);
    }

    /* -------- utility private ---------- */
    private List<Review> loadFromFile() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(f)) {
            Type listType = new TypeToken<List<Review>>() {}.getType();
            List<Review> list = gson.fromJson(r, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void save() {
        try (Writer w = new FileWriter(FILE_PATH)) {
            gson.toJson(reviews, w);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private int nextId() {
        return reviews.stream().mapToInt(Review::getId).max().orElse(0) + 1;
    }

    private int keyToId(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof Integer id))
            throw new IllegalArgumentException("Key must be int id");
        return id;
    }
}