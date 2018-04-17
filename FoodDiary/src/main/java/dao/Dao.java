package dao;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T saveOrUpdate(T object) throws SQLException;

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    void delete(K key) throws SQLException;
}
