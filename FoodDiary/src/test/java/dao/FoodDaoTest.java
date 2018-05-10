/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Food;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class FoodDaoTest {

    Database database;
    FoodDao foodDao;
    Food food;

    public FoodDaoTest() {
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

        Database database = new Database("jdbc:sqlite:db" + File.separator + "fooddiaryTest.db");
        database.init();

        foodDao = new FoodDao(database);
        food = new Food(1, "uusi", 0.0, 0.0, 0.0, 20.0, LocalDate.now());
    }

    @After
    public void tearDown() throws SQLException {
        database.kill();
    }

    @Test
    public void saveOrUpdateWorks() throws SQLException {
        assertEquals(food, foodDao.saveOrUpdate(food));
    }

    @Test
    public void findByNameWorks() throws SQLException {
        foodDao.saveOrUpdate(food);
        assertEquals(food, foodDao.findByName("uusi"));
    }

    @Test
    public void findAllFindsAll() throws SQLException {
        Food first = new Food(1, "first", 0.0, 0.0, 0.0, 0.0, );
        Food second = new Food(1, "another", 0.0, 0.0, 0.0);
        foodDao.saveOrUpdate(first);
        foodDao.saveOrUpdate(second);

        ArrayList<Food> list = new ArrayList<>();

        list.add(first);
        list.add(second);

        List<Food> list2 = foodDao.findAll();

        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.get(0).equals(list2.get(0)));
        }

    }

    @Test
    public void findOneFindsOne() throws SQLException {
        foodDao.saveOrUpdate(food);
        assertEquals(food, foodDao.findOne(1));
    }

    @Test
    public void deleteWorks() throws SQLException {
        Food first = new Food("first", 0.0, 0.0, 0.0);
        Food second = new Food("another", 0.0, 0.0, 0.0);
        foodDao.saveOrUpdate(first);
        foodDao.saveOrUpdate(second);

        foodDao.delete(1);

        for (Food f : foodDao.findAll()) {
            assertTrue(f.equals(second));
        }

    }
}
