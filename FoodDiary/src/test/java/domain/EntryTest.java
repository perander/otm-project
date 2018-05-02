/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.sql.Date;
import java.text.DateFormat;
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
public class EntryTest {
    
    public EntryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void ConstructorWorks(){
        Date date = new Date(1);
        Entry e = new Entry(1, 2, 3, date, 1.0);
        assertEquals(1, e.getId(), 0);
        assertEquals(2, e.getUserId(), 0);
        assertEquals(3, e.getFoodId(), 0);
        assertEquals(date, e.getDate());
        assertEquals(1.0, e.getAmount(), 0);
        
    }
    
    @Test
    public void entriesAreSameWithSameParameters() {
        Entry e = new Entry(1, 2, 3, new Date(1), 1.0);
        Entry e2 = new Entry(1, 2, 3, new Date(1), 1.0);
        
        assertTrue(e.equals(e2));
    }

}
