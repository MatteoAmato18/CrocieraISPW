package dao;

import entity.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityDaoMemory implements GenericDao<Activity> {
    private final List<Activity> activities = new ArrayList<>();

    public ActivityDaoMemory() {
        activities.add(new Activity("Aperitivo", "Goditi l'aperitivo con i nostri gatti"));
        activities.add(new Activity("Gelato", "Mangia il gelato in compagnia"));
    }

    @Override
    public void create(Activity entity) {
        if (read(entity.getName()) == null) {
            activities.add(entity);
        }
    }

    @Override
    public Activity read(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Invalid keys for reading Activity.");
        }
        String name = (String) keys[0];

        return activities.stream()
                .filter(activity -> activity.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Activity entity) {
        Activity existing = read(entity.getName());
        if (existing != null) {
            activities.remove(existing);
            activities.add(entity);
        }
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Invalid keys for deleting Activity.");
        }
        String name = (String) keys[0];

        activities.removeIf(activity -> activity.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Activity> readAll() {
        return new ArrayList<>(activities); // Returns a copy of the list
    }
}