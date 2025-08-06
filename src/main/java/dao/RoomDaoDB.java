package dao;

import entity.Room;
import entity.RoomStatus;
import exception.DataAccessException;
import exception.InvalidDaoKeyException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDaoDB implements GenericDao<Room> {

    /* =====================  Tabella / Colonne  ===================== */
    private static final String TBL           = "Rooms";
    private static final String COL_TYPE      = "type";
    private static final String COL_COST      = "cost";
    private static final String COL_DESC      = "description";
    private static final String COL_MAX_GUEST = "max_guests";
    private static final String COL_STATUS    = "status";

    /* =====================  SQL (letterali)  ======================== */
    private static final String COLS =
            COL_TYPE + "," + COL_COST + "," + COL_DESC + "," + COL_MAX_GUEST + "," + COL_STATUS;

    private static final String SQL_INSERT =
            "INSERT INTO " + TBL + " (" + COLS + ") VALUES (?,?,?,?,?)";

    private static final String SQL_SELECT_ALL =
            "SELECT " + COLS + " FROM " + TBL;

    private static final String SQL_SELECT_BY_TYPE =
            "SELECT " + COLS + " FROM " + TBL + " WHERE " + COL_TYPE + " = ?";

    private static final String SQL_SELECT_FREE =
            "SELECT " + COLS + " FROM " + TBL + " WHERE " + COL_STATUS + " = ?";

    private static final String SQL_UPDATE =
            "UPDATE " + TBL + " SET "
                    + COL_COST + " = ?, "
                    + COL_DESC + " = ?, "
                    + COL_MAX_GUEST + " = ?, "
                    + COL_STATUS + " = ? "
                    + "WHERE " + COL_TYPE + " = ?";

    private static final String SQL_DELETE =
            "DELETE FROM " + TBL + " WHERE " + COL_TYPE + " = ?";

    /* =============================================================== */

    private final Connection connection;

    public RoomDaoDB(Connection connection) { this.connection = connection; }

    /* ============================= CREATE ========================== */
    @Override
    public void create(Room r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {
            ps.setString(1, r.getType());
            ps.setDouble(2, r.getCost());
            ps.setString(3, r.getDescription());
            ps.setInt   (4, r.getMaxGuests());
            ps.setString(5, r.getStatus().name());
            ps.executeUpdate();
        }
    }

    /* ============================== READ =========================== */
    @Override
    public Room read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new InvalidDaoKeyException("Room.read expects a single String (type)");

        try (PreparedStatement ps = connection.prepareStatement(SQL_SELECT_BY_TYPE)) {
            ps.setString(1, (String) keys[0]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ============================= UPDATE ========================== */
    @Override
    public void update(Room r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE)) {
            ps.setDouble(1, r.getCost());
            ps.setString(2, r.getDescription());
            ps.setInt   (3, r.getMaxGuests());
            ps.setString(4, r.getStatus().name());
            ps.setString(5, r.getType());
            ps.executeUpdate();
        }
    }

    /* ============================= DELETE ========================== */
    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new InvalidDaoKeyException("Room.delete expects a single String (type)");

        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE)) {
            ps.setString(1, (String) keys[0]);
            ps.executeUpdate();
        }
    }

    /* ========================= QUERY DI SERVIZIO =================== */
    public List<Room> readFreeRooms() {
        List<Room> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQL_SELECT_FREE)) {
            ps.setString(1, RoomStatus.FREE.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Impossibile recuperare le stanze libere", e);
        }
        return list;
    }

    @Override
    public List<Room> readAll() {
        List<Room> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DataAccessException("Impossibile recuperare l'elenco stanze", e);
        }
        return list;
    }

    /* ============================= Mapper ========================== */
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
