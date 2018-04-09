/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User, Integer> {

    private Database database;

    public UserDao(Database database) {
        this.database = database;
    }

    @Override
    public User findOne(Integer key) throws SQLException {
        User u;
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User WHERE id = ?");
            stmt.setObject(1, key);
            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            
            u = new User(rs.getInt("id"), rs.getString("name"));
            rs.close();
            stmt.close();
        }

        return u;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> annokset = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT id, name FROM User").executeQuery()) {
            while (rs.next()) {
                annokset.add(new User(rs.getInt("id"), rs.getString("name")));
            }
        }
        return annokset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM User WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            
            //delete also users collection (all foods in the collection)
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Entry WHERE user_id = ?");
            stmt2.setInt(1, key);
            stmt2.executeUpdate();

            stmt.close();
            stmt2.close();
        }

    }

    @Override
    public User saveOrUpdate(User u) throws SQLException {
        User byName = findByName(u.getUsername());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO User (name) VALUES (?)");
            stmt.setString(1, u.getUsername());
            stmt.executeUpdate();
        }

        return findByName(u.getUsername());
    }

    public User findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM User WHERE name = ?");
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new User(result.getInt("id"), result.getString("name"));
        }
    }

    //OPTIONAL
    //to find out most popular foods, for example
    public List<User> findByFoodId(Integer id) throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT User.id, User.name "
                        + "FROM Food, User, AnnosRaakaAine "
                        + "WHERE Food.id = " + id + " "
                        + "AND Food.id = Entry.food_id "
                        + "AND User.id = Entry.user_id").executeQuery()) {

            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name")));
            }
        }
        return users;
    }

}
