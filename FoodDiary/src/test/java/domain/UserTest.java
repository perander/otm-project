package domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class UserTest {

    public UserTest() {
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

    @Test
    public void userConstuctorWorks() {
        User u = new User("hello");
        assertTrue(u.getUsername().equals("hello"));
    }

    @Test
    public void userWithIdConstuctorWorks() {
        User u = new User(1, "hello");
        assertTrue(u.getId() == 1);
        assertTrue(u.getUsername().equals("hello"));
    }

    @Test
    public void usersAreDifferentWithDifferentUsername() {
        User user1 = new User("mikko");
        User user2 = new User("teppo");
        assertFalse(user1.equals(user2));
    }

    @Test
    public void usersAreEqualWithSameUsername() {
        User user1 = new User("teppo");
        User user2 = new User("teppo");
        assertTrue(user1.equals(user2));
    }

    @After
    public void tearDown() {
    }

}
