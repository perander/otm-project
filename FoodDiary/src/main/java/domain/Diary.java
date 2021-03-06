/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dao.FoodDao;
import dao.UserDao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Application logic
 */
/**
 *
 * @author sperande
 */
public class Diary {

    private FoodDao foodDao;
    private UserDao userDao;
    private User loggedIn;

    public Diary(FoodDao foodDao, UserDao userDao) {
        this.foodDao = foodDao;
        this.userDao = userDao;
    }

    /**
     * adding a food
     *
     * @param f food to be added
     * @return true if food was added, false if failed
     * @throws SQLException
     */
    public boolean addFood(Food f) throws SQLException {
        try {
            foodDao.saveOrUpdate(f);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * listing all the foods
     *
     * @return all foods
     * @throws SQLException
     */
    public List<Food> getUsersCollection(User user) throws SQLException {
        return foodDao.findByUserId(user.getId());
    }

    /**
     * logging in
     *
     * @param username unique username
     *
     * @return true if username exists, else false
     */
    public boolean login(String username, String password) throws SQLException {
        User user = userDao.findByName(username);
        if (user == null || !(user.getPassword().equals(password))) {
            return false;
        }

        loggedIn = user;

        return true;
    }

    /**
     * user currently logged in
     *
     * @return user logged in
     */
    public User getLoggedUser() {
        return loggedIn;
    }

    /**
     * logging out
     */
    public void logout() {
        loggedIn = null;
    }

    /**
     * signing up / creating an 'account'
     *
     * @param username username
     *
     * @return true if sign up successful, else false
     */
    public boolean createUser(String username, String password) throws SQLException {
        if (userDao.findByName(username) != null) {
            return false;
        }

        User user = new User(username, password);

        try {
            userDao.saveOrUpdate(user);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
