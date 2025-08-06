package dao;

import entity.Room;
import entity.RoomStatus;

import java.util.ArrayList;
import java.util.List;

public class RoomDaoMemory implements GenericDao<Room> {
    private final List<Room> rooms = new ArrayList<>();

    public RoomDaoMemory() {
        rooms.add(new Room("Suite", 1500.00,
                "Suite con balcone e servizio concierge", 2,RoomStatus.FREE));
        rooms.add(new Room("Interior", 499.99,
                "Cabina interna economica", 2,RoomStatus.FREE));
    }

    @Override
    public void create(Room entity) {
        if (read(entity.getType()) == null) rooms.add(entity);
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
        Room existing = read(entity.getType());
        if (existing != null) {
            rooms.remove(existing);
            rooms.add(entity);
        }
    }

    @Override
    public void delete(Object... keys) {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting Room.");
        String type = (String) keys[0];
        rooms.removeIf(r -> r.getType().equalsIgnoreCase(type));
    }

    @Override
    public List<Room> readAll() {
        return new ArrayList<>(rooms);
    }
}
