package dao;

import entity.Room;
import entity.RoomStatus;
import exception.InvalidDaoKeyException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDaoDB implements GenericDao<Room> {

    /* ------------------------------------------------------------------
     *  Costanti per tabella e colonne
     * ------------------------------------------------------------------ */
    private static final String TBL           = "Rooms";
    private static final String COL_TYPE      = "type";
    private static final String COL_COST      = "cost";
    private static final String COL_DESC      = "description";
    private static final String COL_MAX_GUEST = "max_guests";
    private static final String COL_STATUS    = "status";

    /* ------------------------------------------------------------------ */
    private final Connection connection;

    public RoomDaoDB(Connection connection) { this.connection = connection; }

    /* ==================================================================
       CREATE
       ================================================================== */
    @Override
    public void create(Room r) throws SQLException {
        final String sql = "INSERT INTO " + TBL +
                " (" + COL_TYPE + ',' + COL_COST + ',' + COL_DESC + ',' +
                       COL_MAX_GUEST + ',' + COL_STATUS + ") VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, r.getType());
            ps.setDouble(2, r.getCost());
            ps.setString(3, r.getDescription());
            ps.setInt   (4, r.getMaxGuests());
            ps.setString(5, r.getStatus().name());
            ps.executeUpdate();
        }
    }

    /* ==================================================================
       READ (by primary‑key)
       ================================================================== */
    @Override
    public Room read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new InvalidDaoKeyException("Room.read expects a single String (type)");

        String sql = "SELECT * FROM " + TBL + " WHERE " + COL_TYPE + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, (String) keys[0]);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ==================================================================
       UPDATE
       ================================================================== */
    @Override
    public void update(Room r) throws SQLException {
        final String sql = "UPDATE " + TBL + " SET " +
                COL_COST      + " = ?, " +
                COL_DESC      + " = ?, " +
                COL_MAX_GUEST + " = ?, " +
                COL_STATUS    + " = ? " +
                "WHERE " + COL_TYPE + " = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, r.getCost());
            ps.setString(2, r.getDescription());
            ps.setInt   (3, r.getMaxGuests());
            ps.setString(4, r.getStatus().name());  // status 4°
            ps.setString(5, r.getType());           // type   5°
            ps.executeUpdate();
        }
    }

    /* ==================================================================
       DELETE
       ================================================================== */
    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new InvalidDaoKeyException("Room.delete expects a single String (type)");

        String sql = "DELETE FROM " + TBL + " WHERE " + COL_TYPE + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, (String) keys[0]);
            ps.executeUpdate();
        }
    }

    /* ==================================================================
       READ FREE ROOMS
       ================================================================== */
    public List<Room> readFreeRooms() {
        String sql = "SELECT * FROM " + TBL + " WHERE " + COL_STATUS + " = 'FREE'";
        return executeQueryToList(sql);
    }

    /* ==================================================================
       READ ALL
       ================================================================== */
    @Override
    public List<Room> readAll() {
        String sql = "SELECT * FROM " + TBL;
        return executeQueryToList(sql);
    }

    /* ==================================================================
       Helper privati
       ================================================================== */
    private List<Room> executeQueryToList(String sql) {
        List<Room> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve rooms list", e);
        }
        return list;
    }

    private Room map(ResultSet rs) throws SQLException {
        return new Room(
                rs.getString (COL_TYPE),
                rs.getDouble (COL_COST),
                rs.getString (COL_DESC),
                rs.getInt    (COL_MAX_GUEST),
                RoomStatus.valueOf(rs.getString(COL_STATUS))
        );
    }
}

