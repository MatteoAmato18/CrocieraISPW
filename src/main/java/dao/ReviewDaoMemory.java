package dao;

import entity.Review;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** DAO stateless in-memory per Review (thread-safe). */
public class ReviewDaoMemory implements GenericDao<Review> {

    private static final Map<Integer, Review> STORE = new ConcurrentHashMap<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /* --------------- CRUD ---------------- */
    @Override
    public void create(Review r) {
        int id = COUNTER.incrementAndGet();
        r.setId(id);
        STORE.put(id, copy(r));
    }

    @Override
    public Review read(Object... keys) {
        int id = keyToId(keys);
        return copy(STORE.get(id));
    }

    @Override
    public void update(Review r) {
        if (r.getId() == 0 || !STORE.containsKey(r.getId()))
            throw new IllegalArgumentException("Review not found");
        STORE.put(r.getId(), copy(r));
    }

    @Override
    public void delete(Object... keys) {
        STORE.remove(keyToId(keys));
    }

    @Override
    public List<Review> readAll() {
        return STORE.values().stream().map(this::copy).toList();
    }

    /* ----------- utilità private ---------- */
    private int keyToId(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof Integer id))
            throw new IllegalArgumentException("Key must be int id");
        return id;
    }

    private Review copy(Review src) {
        if (src == null) return null;
        Review r = new Review();
        r.setId            (src.getId());
        r.setDate          (src.getDate());
        r.setTime          (src.getTime());
        r.setEmail         (src.getEmail());
        r.setStars         (src.getStars());
        r.setSpecialService(src.getSpecialService());
        r.setBody          (src.getBody());
        return r;
    }
}