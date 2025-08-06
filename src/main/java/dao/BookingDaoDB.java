package dao;

import entity.*;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDaoDB implements GenericDao<Booking> {

    /* ----------------------------  SQL  ---------------------------- */
    private static final String BASE_SELECT = """
        SELECT  id,
                first_name,
                last_name,
                email,
                phone_number,
                room_type,
                destination_name,
                package_name,
                activity_name,
                status
          FROM  bookings
        """;

    private final Connection conn;
    public BookingDaoDB(Connection c) { this.conn = c; }

    /* ---------------------------- CREATE --------------------------- */
    @Override
    public void create(Booking b) throws SQLException {

        final String sql = """
            INSERT INTO bookings
              (first_name, last_name, email, phone_number,
               room_type, destination_name, package_name, activity_name,
               status)
            VALUES (?,?,?,?,?,?,?,?,?)
            """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, b.getFirstName());
            ps.setString(2, b.getLastName());
            ps.setString(3, b.getEmail());
            ps.setString(4, b.getPhoneNumber());
            ps.setString(5, b.getRoomType());
            ps.setString(6, b.getDestinationName());
            ps.setString(7, b.getPackageName());
            ps.setString(8, b.getActivityName());
            ps.setString(9, b.getStatus().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) b.setId(rs.getInt(1));
            }
        }
    }

    /* ----------------------------- READ --------------------------- */
    @Override
    public Booking read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof Integer id))
            throw new IllegalArgumentException("Key must be Integer id");

        final String sql = BASE_SELECT + " WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ---------------------------- UPDATE -------------------------- */
    @Override
    public void update(Booking b) throws SQLException {

        final String sql = """
            UPDATE bookings
               SET first_name       = ?,
                   last_name        = ?,
                   email            = ?,
                   phone_number     = ?,
                   room_type        = ?,
                   destination_name = ?,
                   package_name     = ?,
                   activity_name    = ?,
                   status           = ?
             WHERE id = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getFirstName());
            ps.setString(2, b.getLastName());
            ps.setString(3, b.getEmail());
            ps.setString(4, b.getPhoneNumber());
            ps.setString(5, b.getRoomType());
            ps.setString(6, b.getDestinationName());
            ps.setString(7, b.getPackageName());
            ps.setString(8, b.getActivityName());
            ps.setString(9, b.getStatus().name());
            ps.setInt   (10, b.getId());
            ps.executeUpdate();
        }
    }

    /* ---------------------------- DELETE -------------------------- */
    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof Integer id))
            throw new IllegalArgumentException("Key must be Integer id");

        try (PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM bookings WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* --------------------------- READ ALL ------------------------- */
    @Override
    public List<Booking> readAll() {
        List<Booking> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(BASE_SELECT)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DataAccessException("Errore nel recupero delle prenotazioni", e);
        }
        return list;
    }

    /* --------------------------  MAPPER  -------------------------- */
    private Booking map(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId             (rs.getInt   ("id"));
        b.setFirstName      (rs.getString("first_name"));
        b.setLastName       (rs.getString("last_name"));
        b.setEmail          (rs.getString("email"));
        b.setPhoneNumber    (rs.getString("phone_number"));
        b.setRoomType       (rs.getString("room_type"));
        b.setDestinationName(rs.getString("destination_name"));
        b.setPackageName    (rs.getString("package_name"));
        b.setActivityName   (rs.getString("activity_name"));
        b.setStatus         (BookingStatus.valueOf(rs.getString("status")));
        return b;
    }
}
