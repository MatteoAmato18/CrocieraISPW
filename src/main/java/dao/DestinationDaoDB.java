package dao;

import entity.Destination;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinationDaoDB implements GenericDao<Destination> {
    private final Connection connection;

    public DestinationDaoDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Destination entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Destinations (name, cost) VALUES (?, ?)")) {
            ps.setString(1, entity.getName());
            ps.setDouble(2, entity.getCost());
            ps.executeUpdate();
        }
    }

    @Override
    public Destination read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for reading Destination.");
        String name = (String) keys[0];
        String query = "SELECT name, cost FROM Destinations WHERE name = ?";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Destination(
                            resultSet.getString("name"),
                            resultSet.getDouble("cost")
                            
                    );
                }
            }
        }return null;
    }

    @Override
    public void update(Destination entity) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE Destinations SET cost = ? WHERE name = ?")) {
            ps.setDouble(1, entity.getCost());
            ps.setString(2, entity.getName());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting Destination.");
        String name = (String) keys[0];

        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM Destinations WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Destination> readAll() {
        List<Destination> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT name, cost FROM Destinations");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(new Destination(rs.getString("name"), rs.getDouble("cost")));
        } catch (SQLException e) {
            throw new DataAccessException("Unable to retrieve destinations.", e);
        }
        return list;
    }
}
