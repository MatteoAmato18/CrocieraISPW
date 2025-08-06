package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Destination;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DestinationDaoFile implements GenericDao<Destination> {
    private static final String FILE_PATH = "destinations.json";
    private final Gson gson;
    private List<Destination> destinations;

    public DestinationDaoFile() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        destinations = loadFromFile();
    }

    private List<Destination> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type t = new TypeToken<List<Destination>>(){}.getType();
            List<Destination> list = gson.fromJson(reader, t);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(destinations, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Destination entity) {
        if (read(entity.getName()) != null)
            throw new IllegalArgumentException("Destination already exists: " + entity.getName());
        destinations.add(entity);
        saveToFile();
    }

    @Override
    public Destination read(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for reading Destination.");
        String name = (String) keys[0];
        return destinations.stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Destination entity) {
        for (int i = 0; i < destinations.size(); i++) {
            if (destinations.get(i).getName().equalsIgnoreCase(entity.getName())) {
                destinations.set(i, entity);
                saveToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Destination not found: " + entity.getName());
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting Destination.");
        String name = (String) keys[0];
        destinations.removeIf(d -> d.getName().equalsIgnoreCase(name));
        saveToFile();
    }

    @Override
    public List<Destination> readAll() {
        return new ArrayList<>(destinations);
    }
}
