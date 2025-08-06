package dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import entity.User;
import exception.UserAlreadyInsertedException;
import exception.WrongLoginCredentialsException;


public class UserDaoDB implements GenericDao<User> {
    private static final String TABLE = "`User`";   
    private final Connection connection;

    public UserDaoDB(Connection connection) {
        this.connection = connection;
    }

    /* ---------- CREATE ---------- */
    @Override
    public void create(User user) throws SQLException {
        String sql = "INSERT INTO " + TABLE +
                     " (first_name, last_name, email, phone_number, password) " +
                     "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getPassword());
            ps.executeUpdate();
        }
    }

    /* ---------- READ (by email) ---------- */
    @Override
    public User read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Chiave non valida (email)");

        String email = (String) keys[0];
        String sql = "SELECT first_name, last_name, email, phone_number, password " +
                     "FROM " + TABLE + " WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
            	
                if (rs.next()) {
                    return new User(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getString("password"));
                } else {
                    throw new WrongLoginCredentialsException("Credenziali non corrette");
                }
            }
        }
    }

    /* ---------- UPDATE ---------- */
    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE " + TABLE +
                     " SET first_name = ?, last_name = ?, phone_number = ?, password = ? " +
                     "WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getEmail());   
            ps.executeUpdate();
        }
    }

    /* ---------- DELETE (by email) ---------- */
    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Chiave non valida (email)");

        String email = (String) keys[0];
        String sql = "DELETE FROM " + TABLE + " WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    /* ---------- READ ALL ---------- */
    @Override
    public List<User> readAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT first_name, last_name, email, phone_number, password FROM " + TABLE;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("password")));
            }
        } catch (SQLException e) {
            throw new UserAlreadyInsertedException("Lista non recuperabile", e);
        }
        return users;
    }

 
}