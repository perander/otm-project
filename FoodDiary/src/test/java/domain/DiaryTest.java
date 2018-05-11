/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dao.Database;
import dao.FoodDao;
import dao.UserDao;
import java.sql.SQLException;
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
public class DiaryTest {

    UserDao userDao;
    FoodDao foodDao;
    Diary diary;

    public DiaryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Database database = new Database("jdbc:sqlite:fooddiary.db");
        database.init();

        userDao = new UserDao(database);
        foodDao = new FoodDao(database);
        diary = new Diary(foodDao, userDao);
    }

    @After
    public void tearDown() {
    }

}
