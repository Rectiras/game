package com.example.game;

import com.example.game.model.LeaderboardEntry;
import com.example.game.model.TournamentGroup;
import com.example.game.model.User;
import com.example.game.repository.TournamentGroupRepository;
import com.example.game.repository.UserRepository;
import com.example.game.service.LeaderboardService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LeaderboardServiceTests {

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Mock
    private TournamentGroupRepository tournamentGroupRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetGroupLeaderboardWithValidGroupId() {
        // Mock a tournament group and users
        TournamentGroup mockGroup = new TournamentGroup();
        User user1 = new User();
        user1.setUsername("user1");
        user1.setTournamentScore(100);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setTournamentScore(80);

        User user3 = new User();
        user3.setUsername("user3");
        user3.setTournamentScore(120);

        mockGroup.addMember(user1);
        mockGroup.addMember(user2);
        mockGroup.addMember(user3);

        // Set up mock repository responses
        Mockito.when(tournamentGroupRepository.findById(1L)).thenReturn(java.util.Optional.of(mockGroup));

        // Call the service method
        List<LeaderboardEntry> leaderboard = leaderboardService.getGroupLeaderboard(1L);

        // Verify the results
        assertEquals(3, leaderboard.size());

        // The leaderboard should be sorted by tournament score in descending order
        assertEquals(1, leaderboard.get(0).getRank());
        assertEquals(2, leaderboard.get(1).getRank());
        assertEquals(3, leaderboard.get(2).getRank());

        // Verify the specific user data
        assertEquals("user3", leaderboard.get(0).getUsername());
        assertEquals(120, leaderboard.get(0).getTournamentScore());

        assertEquals("user1", leaderboard.get(1).getUsername());
        assertEquals(100, leaderboard.get(1).getTournamentScore());

        assertEquals("user2", leaderboard.get(2).getUsername());
        assertEquals(80, leaderboard.get(2).getTournamentScore());
    }

    @Test
    public void testGetGroupLeaderboardWithInvalidGroupId() {
        // Mock that the tournament group is not found
        Mockito.when(tournamentGroupRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        // Call the service method with an invalid groupId
        List<LeaderboardEntry> leaderboard = leaderboardService.getGroupLeaderboard(2L);

        // Verify that an empty list is returned
        assertEquals(0, leaderboard.size());
    }
}
