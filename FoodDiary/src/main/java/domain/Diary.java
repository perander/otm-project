/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dao.EntryDao;
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
    private EntryDao entryDao;
    private User loggedIn;

    public Diary(FoodDao foodDao, UserDao userDao, EntryDao entryDao) {
        this.foodDao = foodDao;
        this.userDao = userDao;
        this.entryDao = entryDao;
    }

    //KANNATTAAKO OLLA KAKSI LISÄYSMETODIA, KUN ON KERRAN JO KAKSI FOOD-KONSTRUKTORIA??
    //voisi olla vain 'addFood(Food f)' ja food olisi määritetty ennen lisäystä
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
    //returns ALL the foods -> should be an entry list
    public List<Food> getUsersCollection(User user) throws SQLException {
        System.out.println("user: " + user.getId() + ", " + user.getUsername());
        return foodDao.findByUserId(user.getId());
    }

    /**
     * logging in
     *
     * @param username unique username
     *
     * @return true if username exists, else false
     */
    public boolean login(String username) throws SQLException {
        User user = userDao.findByName(username);
        if (user == null) {
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
    public boolean createUser(String username) throws SQLException {
        if (userDao.findByName(username) != null) {
            return false;
        }

        User user = new User(username);
        try {
            userDao.saveOrUpdate(user);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
