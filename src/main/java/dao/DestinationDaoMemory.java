package dao;

import entity.Destination;
import java.util.ArrayList;
import java.util.List;

public class DestinationDaoMemory implements GenericDao<Destination> {
    private final List<Destination> destinations = new ArrayList<>();

    public DestinationDaoMemory() {
        destinations.add(new Destination("Caraibi", 899.99));
        destinations.add(new Destination("Mediterraneo Orientale", 699.50));
    }

    @Override
    public void create(Destination entity) {
        if (read(entity.getName()) == null) destinations.add(entity);
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
        Destination existing = read(entity.getName());
        if (existing != null) {
            destinations.remove(existing);
            destinations.add(entity);
        }
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting Destination.");
        String name = (String) keys[0];
        destinations.removeIf(d -> d.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<Destination> readAll() {
        return new ArrayList<>(destinations);
    }
}
