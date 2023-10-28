package com.example.game;

import com.example.game.model.User;
import com.example.game.repository.UserRepository;
import com.example.game.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        // Set up test data or perform any necessary setup here
    }

    @Test
    public void testCreateUser() {
        User user = userService.createUser("testUser");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());

        // Print the response
        System.out.println("Response: " + user);
    }

    @Test
    public void testUpdateLevel() {
        // Create a user
        User user = userService.createUser("testUser");

        // Update user's level
        User updatedUser = userService.updateLevel(user.getId());

        assertNotNull(updatedUser);
        assertEquals(user.getLevel() + 1, updatedUser.getLevel());
        assertEquals(user.getCoins() + 25, updatedUser.getCoins());

        // Print the response
        System.out.println("Response: " + updatedUser);
    }

}
