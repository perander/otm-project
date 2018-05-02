/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Entry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntryDao implements Dao<Entry, Integer> {

    private Database database;

    public EntryDao(Database database) {
        this.database = database;
    }

    /**
     * find one entry by its id
     *
     * @param key id of the entry
     * @return entry corresponding to the id, null if not found
     * @throws SQLException
     */
    @Override
    public Entry findOne(Integer key) throws SQLException {
        Entry e;
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Entry WHERE id = ?");
            stmt.setObject(1, key);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            e = new Entry(key, rs.getInt("user_id"), rs.getInt("food_id"),
                    rs.getDate("date"), rs.getDouble("amount"));
            rs.close();
            stmt.close();
        }

        return e;
    }

    /**
     * find all entries from the database
     *
     * @return all the entries
     * @throws SQLException
     */
    @Override
    public List<Entry> findAll() throws SQLException {
        List<Entry> entries = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet rs = conn.prepareStatement("SELECT * FROM Entry").executeQuery()) {
            while (rs.next()) {
                entries.add(new Entry(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("food_id"),
                        rs.getDate("date"), rs.getDouble("amount")));
            }
        }
        return entries;
    }

    /**
     * delete an entry by its id
     *
     * @param key id of the entry to be deleted
     * @throws SQLException
     */
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Entry "
                    + "WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();

            stmt.close();
        }
    }

    /**
     * save or update a new or an existing entry
     *
     * @param e a new entry to be added, or a new entry to be updated
     * @return saved or updated entry
     * @throws SQLException
     */
    @Override
    public Entry saveOrUpdate(Entry e) throws SQLException {

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Entry (user_id, food_id, "
                    + "date, amount) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, e.getUserId());
            stmt.setInt(2, e.getFoodId());
            stmt.setDate(3, e.getDate());
            stmt.setDouble(4, e.getAmount());

            stmt.executeUpdate();
        }

        return e;
    }

    /**
     * find entries entered by a user by users id
     *
     * @param id users id
     * @return list of entries entered by the user
     * @throws SQLException
     */
    //users collection
    public List<Entry> findByUserId(Integer id) throws SQLException {
        List<Entry> entries = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT Entry.id, "
                    + "Entry.user_id, Entry.food_id, Entry.date, Entry.amount "
                    + "FROM Food, Entry, User "
                    + "WHERE Food.id = ? "
                    + "AND User.id = Entry.user_id "
                    + "AND Food.id = Entry.food_id;");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            while (rs.next()) {
                Entry e = new Entry(rs.getInt("id"), id, rs.getInt("food_id"),
                        rs.getDate("date"), rs.getDouble("amount"));
                entries.add(e);
            }

            rs.close();
            stmt.close();
            return entries;
        }
    }

    /**
     * find entries according to the foods added
     *
     * @param id id of the food that is searched
     * @return list of the entries that have the searched food
     * @throws SQLException
     */
    public List<Entry> findByFoodId(Integer id) throws SQLException {
        List<Entry> entries = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT Entry.id, "
                    + "Entry.user_id, Entry.food_id, Entry.date, Entry.amount "
                    + "FROM Food, Entry, User "
                    + "WHERE Food.id = ? "
                    + "AND User.id = Entry.user_id "
                    + "AND Food.id = Entry.food_id;");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }
            while (rs.next()) {
                Entry e = new Entry(rs.getInt("id"), rs.getInt("user_id"), id,
                        rs.getDate("date"), rs.getDouble("amount"));
                entries.add(e);
            }

            rs.close();
            stmt.close();
            return entries;
        }
    }

}
