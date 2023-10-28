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
        // You can set up test data or perform any necessary setup here
    }

    @Test
    public void testCreateUser() {
        // Test the createUser method
        User user = userService.createUser("testUser");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    // Add more test methods for other service methods

}
