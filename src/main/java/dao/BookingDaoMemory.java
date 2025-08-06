package dao;

import entity.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingDaoMemory implements GenericDao<Booking> {

    private static final Map<Integer, Booking> STORE = new ConcurrentHashMap<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /* --------------- CRUD --------------- */
    @Override public void create(Booking b){
        int id = COUNTER.incrementAndGet();
        b.setId(id); STORE.put(id, deepCopy(b));
    }
    @Override public Booking read(Object... k){
        int id = (int) k[0];
        return deepCopy(STORE.get(id));
    }
    @Override public void update(Booking b){
        STORE.put(b.getId(), deepCopy(b));
    }
    @Override public void delete(Object... k){
        int id = (int) k[0];
        STORE.remove(id);
    }
    @Override public List<Booking> readAll(){
        return STORE.values().stream().map(this::deepCopy).toList();
    }

    /* --------------- helper -------------- */
    private Booking deepCopy(Booking src){
        if (src == null) return null;
        Booking b = new Booking();
        b.setId(src.getId());
        b.setFirstName(src.getFirstName());
        b.setLastName(src.getLastName());
        b.setEmail(src.getEmail());
        b.setPhoneNumber(src.getPhoneNumber());
        b.setRoomType(src.getRoomType());
        b.setDestinationName(src.getDestinationName());
        b.setPackageName(src.getPackageName());
        b.setActivityName(src.getActivityName());
        b.setStatus(src.getStatus());
        return b;
    }
}