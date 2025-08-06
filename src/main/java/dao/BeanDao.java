package dao;

import java.sql.SQLException;
import java.util.List;


 /* ---------metodi crud-------  */

public interface BeanDao<T> {
    void create(T bean) throws SQLException;
    T read(Object... keys) throws SQLException;
    void update(T bean) throws SQLException;
    void delete(Object... keys) throws SQLException;
    List<T> readAll();
}
