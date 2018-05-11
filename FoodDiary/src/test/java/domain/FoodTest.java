/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.time.LocalDate;
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
public class FoodTest {

    public FoodTest() {
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
    public void ConstructorWorks() {
        Food f = new Food(1, 1, "new", 1.0, 2.0, 3.0, 10.0, LocalDate.now());
        
        assertEquals(1, f.getId(), 0);
        assertEquals(1, f.getUserId(), 0);
        assertEquals("new", f.getName());
        assertEquals(1.0, f.getCarb(), 0);
        assertEquals(2.0, f.getProtein(), 0);
        assertEquals(3.0, f.getFat(), 0);
        assertEquals(10.0, f.getAmount(), 0);
        assertEquals(LocalDate.now(), f.getDate());

    }

    @Test
    public void foodsAreEqualWithSameParameters() {
        Food f = new Food(1, "new", 1.0, 2.0, 3.0, 10.0, LocalDate.now());
        Food f2 = new Food(1, "new", 1.0, 2.0, 3.0, 10.0, LocalDate.now());

        assertTrue(f.equals(f2));
    }

}
