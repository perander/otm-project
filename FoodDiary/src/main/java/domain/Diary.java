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

    //only adds a food with a name
    public boolean addFood(String name){
        Food f = new Food(name, 0.0, 0.0, 0.0);
        try {
            foodDao.saveOrUpdate(f);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    //adds a food with nutritional value
    public boolean addFoodTotal(String name, Double carb, Double protein, Double fat) {
        Food f = new Food(name, carb, protein, fat);
        try {
            foodDao.saveOrUpdate(f);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    //returns ALL the foods -> should be an entry list
    public List<Food> getUsersCollection() throws SQLException {
        return foodDao.findAll();
    }

    /**
     * logging in
     *
     * @param username username
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
