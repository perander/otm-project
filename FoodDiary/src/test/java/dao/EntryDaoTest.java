/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Entry;
import domain.Food;
import java.sql.Date;
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
public class EntryDaoTest {
    Database database;
    EntryDao entryDao;
    Entry entry;
    
    public EntryDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        database = new Database("jdbc:sqlite:fooddiaryTest.db");
        database.init();

        entryDao = new EntryDao(database);
        entry = new Entry(1, 2, 3, new Date(1), 1.0);
    }
    
    @After
    public void tearDown() throws SQLException {
        database.kill();
    }

    @Test
    public void saveOrUpdateWorks() throws SQLException{
        assertEquals(entry, entryDao.saveOrUpdate(entry));
    }
    
    @Test
    public void findAllFindsAll() throws SQLException {
        Entry e = new Entry(1, 2, 3, new Date(1), 1.0);
        Entry e2 = new Entry(2, 3, 4, new Date(1), 1.0);
        entryDao.saveOrUpdate(e);
        entryDao.saveOrUpdate(e2);

        ArrayList<Entry> list = new ArrayList<>();

        list.add(e);
        list.add(e2);

        List<Entry> list2 = entryDao.findAll();

        for (int i = 0; i < list.size(); i++) {
            assertTrue(list.get(0).equals(list2.get(0)));
        }

    }
    
}
