/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Food;
import domain.User;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sperande
 */
public class UserDaoTest {

    UserDao userDao;
    Database database;

    User user;

    public UserDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        File dbDirectory = new File("dbTest");
        dbDirectory.mkdir();

        database = new Database("jdbc:sqlite:dbTest" + File.separator + "fooddiaryTest.db");
        database.init();

        userDao = new UserDao(database);
        user = new User(1, "new", "password");
    }

    @After
    public void tearDown() throws SQLException {
        database.kill();
    }

    @Test
    public void saveOrUpdateWorks() throws SQLException {
        assertEquals(user, userDao.saveOrUpdate(user));
    }

    @Test
    public void findByNameWorks() throws SQLException {
        userDao.saveOrUpdate(user);
        assertEquals(user, userDao.findByName("new"));
    }
    
    @Test
    public void findByNameReturnsNull() throws SQLException {
        assertEquals(null, userDao.findByName("new"));
    }

    @Test
    public void findAllFindsAll() throws SQLException {
        User first = new User(1, "first", "password1");
        User second = new User(2, "another", "password2");
        userDao.saveOrUpdate(first);
        userDao.saveOrUpdate(second);

        ArrayList<User> list = new ArrayList<>();

        list.add(first);
        list.add(second);

        List<User> list2 = userDao.findAll();

        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.get(0).equals(list2.get(0)));
        }

    }

    @Test
    public void findOneFindsOne() throws SQLException {
        userDao.saveOrUpdate(user);
        assertEquals(user, userDao.findOne(1));
    }

    @Test
    public void deleteWorks() throws SQLException {
        User first = new User(1, "first", "password");
        User second = new User(2, "another", "password");
        userDao.saveOrUpdate(first);
        userDao.saveOrUpdate(second);

        userDao.delete(1);

        for (User u : userDao.findAll()) {
            assertEquals(second, u);
        }

    }

}
