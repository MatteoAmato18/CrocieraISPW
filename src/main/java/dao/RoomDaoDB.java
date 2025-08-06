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

    /* =====================  Frammenti SQL  ========================= */
    private static final String SEP          = ", ";
    private static final String EQ_QM        = " = ?";
    private static final String KW_WHERE     = " WHERE ";
    private static final String KW_SET       = " SET ";

    /** SELECT con colonne esplicite (evita `SELECT *`). */
    private static final String COLS = String.join(SEP,
            COL_TYPE, COL_COST, COL_DESC, COL_MAX_GUEST, COL_STATUS);
    private static final String BASE_SELECT = "SELECT " + COLS + " FROM " + TBL;

    /* =============================================================== */

    private final Connection connection;

    public RoomDaoDB(Connection connection) {
        this.connection = connection;
    }

    /* ============================= CREATE ========================== */
    @Override
    public void create(Room r) throws SQLException {
        final String sql = "INSERT INTO " + TBL + " (" + String.join(SEP,
                COL_TYPE, COL_COST, COL_DESC, COL_MAX_GUEST, COL_STATUS) + ") VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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

        final String sql = BASE_SELECT + KW_WHERE + COL_TYPE + EQ_QM;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, (String) keys[0]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ============================= UPDATE ========================== */
    @Override
    public void update(Room r) throws SQLException {
        final String setClause = String.join(SEP,
                COL_COST + EQ_QM,
                COL_DESC + EQ_QM,
                COL_MAX_GUEST + EQ_QM,
                COL_STATUS + EQ_QM);

        final String sql = "UPDATE " + TBL + KW_SET + setClause
                         + KW_WHERE + COL_TYPE + EQ_QM;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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

        final String sql = "DELETE FROM " + TBL + KW_WHERE + COL_TYPE + EQ_QM;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, (String) keys[0]);
            ps.executeUpdate();
        }
    }

    /* ========================= QUERY DI SERVIZIO =================== */
    /** Solo stanze con status FREE. */
    public List<Room> readFreeRooms() {
        final String sql = BASE_SELECT + KW_WHERE + COL_STATUS + EQ_QM;
        return queryToList(sql, ps -> ps.setString(1, RoomStatus.FREE.name()));
    }

    @Override
    public List<Room> readAll() {
        return queryToList(BASE_SELECT, null);
    }

    /** Funzionale semplice per il binding dei parametri. */
    @FunctionalInterface
    private interface Binder { void bind(PreparedStatement ps) throws SQLException; }

    private List<Room> queryToList(String sql, Binder binder) {
        List<Room> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (binder != null) binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Impossibile recuperare l'elenco stanze", e);
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
