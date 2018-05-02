/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dao.Database;
import dao.EntryDao;
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
    EntryDao entryDao;
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
    public void setUp() throws ClassNotFoundException {
        Database database = new Database("jdbc:sqlite:fooddiary.db");
        database.init();

        userDao = new UserDao(database);
        foodDao = new FoodDao(database);
        entryDao = new EntryDao(database);
        diary = new Diary(foodDao, userDao, entryDao);
    }
    
    @After
    public void tearDown() {
    }

//    @Test
//    public void FoodIsAdded() throws SQLException{
//        Food f = new Food("new food", 1.0, 1.0, 1.0);
//        diary.addFood(f);
//        assertEquals(foodDao.findByName("new food"), f);
//    }
   
    
}
