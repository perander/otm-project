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

    /**
     * find a user by the users id
     *
     * @param key users id
     * @return user with the id, null if not found
     * @throws SQLException
     */
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

            u = new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
            rs.close();
            stmt.close();
        }

        return u;
    }

    /**
     * find all users
     *
     * @return a list of all users
     * @throws SQLException
     */
    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT * FROM User").executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("password")));
            }
        }
        return users;
    }

    
    //NEVER USED
    
    /**
     * delete a user by a user id
     *
     * @param key user id
     * @throws SQLException
     */
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM User WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();

            //delete also users collection (all foods in the collection)
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Food WHERE UserId = ?");
            stmt2.setInt(1, key);
            stmt2.executeUpdate();

            stmt.close();
            stmt2.close();
        }

    }

    /**
     * save a new user or update a user
     *
     * @param u user to be saved or updated
     * @return the user to be saved or updated
     * @throws SQLException
     */
    @Override
    public User saveOrUpdate(User u) throws SQLException {
        User byName = findByName(u.getUsername());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO User (name, password) VALUES (?, ?)");
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            
            stmt.executeUpdate();
        }

        return findByName(u.getUsername());
    }

    /**
     * find a user by the username
     *
     * @param name users username
     * @return user with the username, null if not found
     * @throws SQLException
     */
    public User findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name, password FROM User WHERE name = ?");
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new User(result.getInt("id"), result.getString("name"), result.getString("password"));
        }
    }

    /**
     * find users who have entered a specific food
     *
     * @param id id of the food
     * @return a list of users who have entered the food
     * @throws SQLException
     */
    //OPTIONAL
    //to find out most popular foods, for example
//    public List<User> findByFoodId(Integer id) throws SQLException {
//        List<User> users = new ArrayList<>();
//
//        try (Connection conn = database.getConnection();
//                
//                ResultSet rs = conn.prepareStatement("SELECT User.id, User.name "
//                        + "FROM Food, User, AnnosRaakaAine "
//                        + "WHERE Food.id = " + id + " "
//                        + "AND Food.id = Entry.food_id "
//                        + "AND User.id = Entry.user_id").executeQuery()) {
//
//            while (rs.next()) {
//                users.add(new User(rs.getInt("id"), rs.getString("name")));
//            }
//        }
//        return users;
//    }

}
