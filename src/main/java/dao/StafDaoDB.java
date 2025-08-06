package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Staf;
import exception.UserAlreadyInsertedException;
import exception.WrongLoginCredentialsException;

public class StafDaoDB implements GenericDao<Staf> {
    private final Connection connection;

    public StafDaoDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Staf staf) throws SQLException {
        String sql = "INSERT INTO Staf (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, staf.getFirstName());
            ps.setString(2, staf.getLastName());
            ps.setString(3, staf.getEmail());
            
            ps.setString(4, staf.getPassword());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException _) {
            throw new UserAlreadyInsertedException("Esiste già un staffer con questa email");
        }
    }

    @Override
    public Staf read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Devi fornire un solo parametro di tipo String (email).");
        }
        String email = (String) keys[0];

        String sql = "SELECT Staf_first_name, Staflast_name, Stafemail, Stafpassword FROM Staf WHERE Stafemail = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Staf(
                            rs.getString("Staf_first_name"),
                            rs.getString("Staflast_name"),
                            rs.getString("Stafemail"),
                            
                            rs.getString("Stafpassword")
                    );
                } else {
                    throw new WrongLoginCredentialsException("Credenziali non corrette");
                }
            }
        }
    }

    @Override
    public void update(Staf staf) throws SQLException {
        String sql = "UPDATE Staf SET Staf_first_name = ?, last_name = ?, password = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, staf.getFirstName());
            ps.setString(2, staf.getLastName());
            
            ps.setString(3, staf.getPassword());
            ps.setString(4, staf.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Devi fornire un solo parametro di tipo String (email).");
        }
        String email = (String) keys[0];

        String sql = "DELETE FROM Staf WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Staf> readAll() {
        List<Staf> staf = new ArrayList<>();
        String sql = "SELECT first_name, last_name, email, password FROM Staf";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
            	staf.add(new Staf(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                       
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            throw new UserAlreadyInsertedException("Lista non recuperabile, ", e);
        }
        return staf;
    }
}