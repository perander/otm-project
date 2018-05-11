/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Food;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodDao implements Dao<Food, Integer> {

    private Database database;

    public FoodDao(Database database) {
        this.database = database;
    }

    /**
     * find all foods from the database
     *
     * @return a list of all the foods
     * @throws SQLException
     */
    @Override
    public List<Food> findAll() throws SQLException {
        List<Food> foods = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet rs = conn.prepareStatement(
                        "SELECT * FROM Food").executeQuery()) {
            while (rs.next()) {
                foods.add(new Food(rs.getInt("id"), rs.getInt("userId"), rs.getString("name"), rs.getDouble("carb"),
                        rs.getDouble("protein"), rs.getDouble("fat"),
                        rs.getDouble("amount"), rs.getDate("date").toLocalDate()));
            }
        }
        return foods;
    }

    /**
     * delete a food by its id
     *
     * @param key id of the food to be deleted
     * @throws SQLException
     */
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Food WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            stmt.close();
        }

    }

    /**
     * save a new food or update an existing one
     *
     * @param f food to be saved or updated
     * @return the saved or updated food
     * @throws SQLException
     */
    @Override
    public Food saveOrUpdate(Food f) throws SQLException {
       
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Food (userId, name, carb, protein, fat, amount, date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, f.getUserId());
            stmt.setString(2, f.getName());
            stmt.setDouble(3, f.getCarb());
            stmt.setDouble(4, f.getProtein());
            stmt.setDouble(5, f.getFat());
            stmt.setDouble(6, f.getAmount());
            stmt.setDate(7, java.sql.Date.valueOf(f.getDate()));

            stmt.executeUpdate();
        }

        return findByName(f.getName());
    }

    /**
     * finds all food added by a user by the username
     *
     * @param id users id
     * @return list of foods added by the user
     * @throws SQLException
     */
    public List<Food> findByUserId(Integer id) throws SQLException {

        List<Food> foods = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, userId, name, carb, protein, fat, amount, date "
                    + "FROM Food WHERE userId = ? ORDER BY date DESC");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            while (rs.next()) {
                Food f = new Food(rs.getInt("id"), rs.getInt("userId"), rs.getString("name"), rs.getDouble("carb"),
                        rs.getDouble("protein"), rs.getDouble("fat"),
                        rs.getDouble("amount"), rs.getDate("date").toLocalDate());

                foods.add(f);
            }

            rs.close();
            stmt.close();
            return foods;
        }
    }

    /**
     * find a food by its name
     *
     * @param name food name to be searched
     * @return a food with the name, null if not found
     * @throws SQLException
     */
    public Food findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, userId, name, carb, protein, fat, amount, date "
                    + "FROM Food WHERE name = ?");
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return new Food(rs.getInt("id"), rs.getInt("userId"), rs.getString("name"), rs.getDouble("carb"),
                    rs.getDouble("protein"), rs.getDouble("fat"),
                    rs.getDouble("amount"), rs.getDate("date").toLocalDate());
        }
    }

    /**
     * find a food by its id
     *
     * @param key food id
     * @return food with the id, null if not found
     * @throws SQLException
     */
    @Override
    public Food findOne(Integer key) throws SQLException {
        Food f;
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Food WHERE id = ?");
            stmt.setObject(1, key);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            f = new Food(rs.getInt("id"), rs.getInt("userId"), rs.getString("name"), rs.getDouble("carb"),
                    rs.getDouble("protein"), rs.getDouble("fat"),
                    rs.getDouble("amount"), rs.getDate("date").toLocalDate());
            rs.close();
            stmt.close();
        }

        return f;
    }

}
