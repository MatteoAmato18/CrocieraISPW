package dao;

import entity.Activity;
import exception.UserAlreadyInsertedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityDaoDB implements GenericDao<Activity> {
    private final Connection connection;

    public ActivityDaoDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Activity entity) throws SQLException {
        String query = "INSERT INTO Activities (name, description) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            
            statement.executeUpdate();
        }
    }

    @Override
    public Activity read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Invalid keys for reading Activity.");
        }
        String name = (String) keys[0];

        String query = "SELECT name, description FROM Activities WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Activity(
                            resultSet.getString("name"),
                            resultSet.getString("description")
                            
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void update(Activity entity) throws SQLException {
        String query = "UPDATE Activities SET description = ?, max_participants = ? WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getDescription());
            
            statement.setString(3, entity.getName());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String)) {
            throw new IllegalArgumentException("Invalid keys for deleting Activity.");
        }
        String name = (String) keys[0];

        String query = "DELETE FROM Activities WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Activity> readAll() {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT name, description FROM Activities";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                activities.add(new Activity(
                        resultSet.getString("name"),
                        resultSet.getString("description")
                        
                ));
            }
        } catch (SQLException e) {
            throw new UserAlreadyInsertedException("Unable to retrieve activities list.", e);
        }
        return activities;
    }
}