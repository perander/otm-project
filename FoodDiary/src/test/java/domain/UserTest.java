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
        User u = new User("hello", "password");
        assertTrue(u.getUsername().equals("hello"));
        assertTrue(u.getPassword().equals("password"));
    }

    @Test
    public void userWithIdConstuctorWorks() {
        User u = new User(1, "hello", "password");
        assertTrue(u.getId() == 1);
        assertTrue(u.getUsername().equals("hello"));
        assertTrue(u.getPassword().equals("password"));
    }

    @Test
    public void usersAreDifferentWithDifferentUsername() {
        User user1 = new User("mikko", "password");
        User user2 = new User("teppo", "password");
        assertFalse(user1.equals(user2));
    }

    @Test
    public void usersAreEqualWithSameUsernameAndPassword() {
        User user1 = new User("teppo", "password");
        User user2 = new User("teppo", "password");
        assertTrue(user1.equals(user2));
    }
    
   

    @After
    public void tearDown() {
    }

}
