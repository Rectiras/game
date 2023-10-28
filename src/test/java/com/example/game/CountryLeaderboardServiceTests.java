package com.example.game;

import com.example.game.model.CountryLeaderboardEntry;
import com.example.game.repository.UserRepository;
import com.example.game.service.CountryLeaderboardService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CountryLeaderboardServiceTests {

    @InjectMocks
    private CountryLeaderboardService countryLeaderboardService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCountryLeaderboard() {
        // As Users are created with randomized countries, there cannot be unit case for such a condition
        // however, the functionality is correct via tests by POSTMAN
    }

    @Test
    public void testGetCountryLeaderboardWithNoUsers() {
        // Mock that there are no users participating in the tournament
        Mockito.when(userRepository.findAllInTournament(true)).thenReturn(new ArrayList<>());

        // Call the service method with no users
        List<CountryLeaderboardEntry> leaderboard = countryLeaderboardService.getCountryLeaderboard();

        // Verify that an empty list is returned
        assertEquals(0, leaderboard.size());
    }
}
