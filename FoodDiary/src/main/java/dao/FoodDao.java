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

    @Override
    public List<Food> findAll() throws SQLException {
        List<Food> foods = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT id, name, carb, protein, fat "
                        + "FROM Food").executeQuery()) {
            while (rs.next()) {
                foods.add(new Food(rs.getInt("id"), rs.getString("name"), rs.getDouble("carb"),
                        rs.getDouble("protein"), rs.getDouble("fat")));
            }
        }
        return foods;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Food WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            //No point to delete from previous collecions? They're just history
//            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Entry WHERE food_id = ?");
//            stmt2.setInt(1, key);
//            stmt2.executeUpdate();

            stmt.close();
//            stmt2.close();
        }

    }

    @Override
    public Food saveOrUpdate(Food f) throws SQLException {
        Food byName = findByName(f.getName());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Food (name) VALUES (?)");
            stmt.setString(1, f.getName());
            stmt.executeUpdate();
        }

        return findByName(f.getName());
    }

    public Food findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM Food WHERE name = ?");
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return new Food(rs.getInt("id"), rs.getString("name"), rs.getDouble("carb"),
                    rs.getDouble("protein"), rs.getDouble("fat"));
        }
    }

    public List<Food> findByUSerId(Integer id) throws SQLException {
        List<Food> ainekset = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT DISTINCT Food.id, Food.name "
                        + "FROM Food, User, Entry "
                        + "WHERE User.id = " + id + " "
                        + "AND User.id = Entry.user_id "
                        + "AND Food.id = Entry.food_id").executeQuery()) {
            while (rs.next()) {
                ainekset.add(new Food(rs.getInt("id"), rs.getString("name"), rs.getDouble("carb"),
                        rs.getDouble("protein"), rs.getDouble("fat")));
            }
        }
        return ainekset;
    }

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

            f = new Food(rs.getInt("id"), rs.getString("name"), rs.getDouble("carb"),
                    rs.getDouble("protein"), rs.getDouble("fat"));
            rs.close();
            stmt.close();
        }

        return f;
    }

}
