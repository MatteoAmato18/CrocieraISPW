package dao;

import entity.Room;
import entity.RoomStatus;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDaoDB implements GenericDao<Room> {

    /* ----------------------------  SQL  ---------------------------- */
    private static final String BASE_SELECT = """
        SELECT  type,
                cost,
                description,
                max_guests,
                status
          FROM  Rooms
        """;

    private final Connection conn;
    public RoomDaoDB(Connection c) { this.conn = c; }

    /* ---------------------------- CREATE --------------------------- */
    @Override
    public void create(Room r) throws SQLException {
        final String sql = """
            INSERT INTO Rooms
              (type, cost, description, max_guests, status)
            VALUES (?,?,?,?,?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getType());
            ps.setDouble(2, r.getCost());
            ps.setString(3, r.getDescription());
            ps.setInt   (4, r.getMaxGuests());
            ps.setString(5, r.getStatus().name());
            ps.executeUpdate();
        }
    }

    /* ----------------------------- READ --------------------------- */
    @Override
    public Room read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String type))
            throw new IllegalArgumentException("Key must be String room type");

        final String sql = BASE_SELECT + " WHERE type = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ---------------------------- UPDATE -------------------------- */
    @Override
    public void update(Room r) throws SQLException {
        final String sql = """
            UPDATE Rooms
               SET cost        = ?,
                   description = ?,
                   max_guests  = ?,
                   status      = ?
             WHERE type = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, r.getCost());
            ps.setString(2, r.getDescription());
            ps.setInt   (3, r.getMaxGuests());
            ps.setString(4, r.getStatus().name());
            ps.setString(5, r.getType());
            ps.executeUpdate();
        }
    }

    /* ---------------------------- DELETE -------------------------- */
    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String type))
            throw new IllegalArgumentException("Key must be String room type");

        try (PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM Rooms WHERE type = ?")) {
            ps.setString(1, type);
            ps.executeUpdate();
        }
    }

    /* --------------------------- READ ALL ------------------------- */
    @Override
    public List<Room> readAll() {
        List<Room> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(BASE_SELECT)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero delle stanze", e);
        }
        return list;
    }

    /* ---------------------- READ FREE (utility) ------------------- */
    /** Restituisce solo le stanze con stato FREE. */
    public List<Room> readFreeRooms() {
        List<Room> list = new ArrayList<>();
        final String sql = BASE_SELECT + " WHERE status = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, RoomStatus.FREE.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero delle stanze libere", e);
        }
        return list;
    }

    /* --------------------------  MAPPER  -------------------------- */
    private Room map(ResultSet rs) throws SQLException {
        return new Room(
            rs.getString ("type"),
            rs.getDouble ("cost"),
            rs.getString ("description"),
            rs.getInt    ("max_guests"),
            RoomStatus.valueOf(rs.getString("status"))
        );
    }
}
