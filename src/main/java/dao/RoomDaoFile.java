package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Room;
import entity.RoomStatus;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RoomDaoFile implements GenericDao<Room> {
    private static final String FILE_PATH = "rooms.json";
    private final Gson gson;
    private List<Room> rooms;

    public RoomDaoFile() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        rooms = loadFromFile();
    }

    private List<Room> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Room>>(){}.getType();
            List<Room> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(rooms, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Room entity) {
        if (read(entity.getType()) != null)
            throw new IllegalArgumentException("Room already exists: " + entity.getType());
        rooms.add(entity);
        saveToFile();
    }

    @Override
    public Room read(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for reading Room.");
        String type = (String) keys[0];
        return rooms.stream()
                .filter(r -> r.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);
    }
    public List<Room> readFreeRooms() {
        List<Room> free = new ArrayList<>();
        for (Room r : rooms) {
            if (r.getStatus() == RoomStatus.FREE) free.add(r);
        }
        return free;
    }

    @Override
    public void update(Room entity) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getType().equalsIgnoreCase(entity.getType())) {
                rooms.set(i, entity);
                saveToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Room not found: " + entity.getType());
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting Room.");
        String type = (String) keys[0];
        rooms.removeIf(r -> r.getType().equalsIgnoreCase(type));
        saveToFile();
    }

    @Override
    public List<Room> readAll() {
        return new ArrayList<>(rooms);
    }
}
