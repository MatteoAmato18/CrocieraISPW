package dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import entity.Booking;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingDaoFile implements GenericDao<Booking> {

    private static final String FILE_PATH = "bookings.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Booking> list;
    private final AtomicInteger counter;      // id auto‑increment

    public BookingDaoFile() {
        list = loadFromDisk();
        counter = new AtomicInteger(list.stream()
                                        .mapToInt(Booking::getId)
                                        .max().orElse(0));
    }

    /* -------------------- I/O helper --------------------- */
    private List<Booking> loadFromDisk() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(f)) {
            Type t = new TypeToken<List<Booking>>(){}.getType();
            List<Booking> l = gson.fromJson(r, t);
            return l != null ? l : new ArrayList<>();
        } catch (IOException e){ e.printStackTrace(); return new ArrayList<>(); }
    }
    private void save(){ try (Writer w=new FileWriter(FILE_PATH)){ gson.toJson(list,w);}catch(IOException e){e.printStackTrace();} }

    /* ---------------------- CRUD ------------------------ */
    @Override public void create(Booking b){
        b.setId(counter.incrementAndGet());
        list.add(b); save();
    }
    @Override public Booking read(Object... k){
        int id = (int) k[0];
        return list.stream().filter(b -> b.getId()==id).findFirst().orElse(null);
    }
    @Override public void update(Booking b){
        delete(b.getId()); list.add(b); save();
    }
    @Override public void delete(Object... k){
        int id = (int) k[0];
        list.removeIf(b -> b.getId()==id); save();
    }
    @Override public List<Booking> readAll(){ return new ArrayList<>(list); }
}