package dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T> {
    void create(T entity) throws SQLException;
    T read(Object... keys) throws SQLException; // Permette più chiavi
    void update(T entity) throws SQLException;
    void delete(Object... keys) throws SQLException; // Permette più chiavi
    List<T> readAll();
}