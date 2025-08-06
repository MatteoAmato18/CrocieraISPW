package dao;


import entity.CruisePackage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CruisePackageDaoDB implements GenericDao<CruisePackage> {
    private final Connection connection;

    public CruisePackageDaoDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(CruisePackage entity) throws SQLException {
        String q = "INSERT INTO CruisePackages (name, cost) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setString(1, entity.getName());
            ps.setDouble(2, entity.getCost());
            ps.executeUpdate();
        }
    }

    @Override
    public CruisePackage read(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for reading CruisePackage.");
        String name = (String) keys[0];

        String q = "SELECT name, cost  FROM CruisePackages WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(q)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new CruisePackage(
                            resultSet.getString("name"),
                            resultSet.getDouble("cost")
                            
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void update(CruisePackage entity) throws SQLException {
        String q = "UPDATE CruisePackages SET cost = ? WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(q)) {
            ps.setDouble(1, entity.getCost());
            ps.setString(2, entity.getName());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object... keys) throws SQLException {
        if (keys.length != 1 || !(keys[0] instanceof String))
            throw new IllegalArgumentException("Invalid keys for deleting CruisePackage.");
        String name = (String) keys[0];

        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM CruisePackages WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    @Override
    public List<CruisePackage> readAll() {
        List<CruisePackage> list = new ArrayList<>();
        String q = "SELECT name, cost  FROM CruisePackages";
        try (PreparedStatement statement = connection.prepareStatement(q);
                ResultSet resultSet = statement.executeQuery()) {
               while (resultSet.next()) {
                   list.add(new CruisePackage(
                           resultSet.getString("name"),
                           resultSet.getDouble("cost")
                           
                   ));
               }
           } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve cruise packages.", e);
        }
        return list;
    }
}
