package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Activity;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityDaoFile implements GenericDao<Activity> {
    private static final String FILE_PATH = "activities.json";
    private final Gson gson;
    private List<Activity> activities;

    public ActivityDaoFile() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // Registra l'adapter
                .setPrettyPrinting()
                .create();
        activities = loadFromFile();
    }

    private List<Activity> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Activity>>() {}.getType();
            List<Activity> loadedActivities = gson.fromJson(reader, listType);
            return loadedActivities != null ? loadedActivities : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(activities, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Activity entity) {
        if (read(entity.getName()) != null) {
            throw new IllegalArgumentException("Activity already exists: " + entity.getName());
        }
        activities.add(entity);
        saveToFile();
    }

    @Override
    public Activity read(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Invalid keys for reading Activity.");
        }
        String name = (String) keys[0];

        return activities.stream()
                .filter(activity -> activity.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Activity entity) {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getName().equals(entity.getName())) {
                activities.set(i, entity);
                saveToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Activity not found: " + entity.getName());
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Invalid keys for deleting Activity.");
        }
        String name = (String) keys[0];

        activities.removeIf(activity -> activity.getName().equals(name));
        saveToFile();
    }

    @Override
    public List<Activity> readAll() {
        return new ArrayList<>(activities);
    }
}