package dao;

import entity.Review;
import exception.DataAccessException;    // <-- import dedicato

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO JDBC per la tabella <code>reviews</code> (senza SELECT *). */
public class ReviewDaoDB implements GenericDao<Review> {

    private static final String BASE_SELECT = """
        SELECT  id,
                review_date,
                review_time,
                email,
                stars,
                specialService,
                body
          FROM  reviews
        """;

    private final Connection conn;
    public ReviewDaoDB(Connection c) { this.conn = c; }

    /* ---------------- CREATE ---------------- */
    @Override
    public void create(Review r) throws SQLException {
        final String sql = """
            INSERT INTO reviews
              (review_date, review_time, email, stars, specialService, body)
            VALUES (?,?,?,?,?,?)
            """;
        try (PreparedStatement ps =
                 conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate (1, Date.valueOf(r.getDate()));
            ps.setTime (2, Time.valueOf(r.getTime()));
            ps.setString(3, r.getEmail());
            ps.setInt   (4, r.getStars());
            ps.setString(5, r.getSpecialService());
            ps.setString(6, r.getBody());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getInt(1));
            }
        }
    }

    /* ---------------- READ ------------------ */
    @Override
    public Review read(Object... keys) throws SQLException {
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

    /* ---------------- UPDATE ---------------- */
    @Override
    public void update(Review r) throws SQLException {
        final String sql = """
            UPDATE reviews
               SET review_date            = ?,
                   review_time             = ?,
                   email           = ?,
                   stars           = ?,
                   specialService  = ?,
                   body            = ?
             WHERE id = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate (1, Date.valueOf(r.getDate()));
            ps.setTime (2, Time.valueOf(r.getTime()));
            ps.setString(3, r.getEmail());
            ps.setInt   (4, r.getStars());
            ps.setString(5, r.getSpecialService());
            ps.setString(6, r.getBody());
            ps.setInt   (7, r.getId());
            ps.executeUpdate();
        }
    }

    /* ---------------- DELETE ---------------- */
    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof Integer id))
            throw new IllegalArgumentException("Key must be Integer id");

        try (PreparedStatement ps =
                 conn.prepareStatement("DELETE FROM reviews WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* ---------------- READ ALL --------------- */
    @Override
    public List<Review> readAll() {
        List<Review> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(BASE_SELECT)) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new DataAccessException("Impossibile leggere tutte le recensioni", e);
        }
        return list;
    }

    /* ---------------- mapper ---------------- */
    private Review map(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId            (rs.getInt   ("id"));
        r.setDate          (rs.getDate  ("data").toLocalDate());
        r.setTime          (rs.getTime  ("ora").toLocalTime());
        r.setEmail         (rs.getString("email"));
        r.setStars         (rs.getInt   ("stars"));
        r.setSpecialService(rs.getString("specialService"));
        r.setBody          (rs.getString("body"));
        return r;
    }
}
