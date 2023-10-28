package com.example.game;

import com.example.game.model.TournamentGroup;
import com.example.game.model.User;
import com.example.game.repository.TournamentGroupRepository;
import com.example.game.repository.UserRepository;
import com.example.game.service.LeaderboardService;
import com.example.game.service.TournamentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TournamentServiceTests {

    @InjectMocks
    @Autowired
    private TournamentService tournamentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TournamentGroupRepository tournamentGroupRepository;

    @Mock
    private LeaderboardService leaderboardService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEnterTournament_WithValidUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setAvailableTournament(true);
        user.setLevel(20);
        user.setCoins(1000);
        user.setTournamentRewardsClaimed(true);
        user.setInTournament(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        TournamentGroup group = new TournamentGroup();
        when(tournamentGroupRepository.findByGroupFullFalse()).thenReturn(Collections.singletonList(group));

        ResponseEntity<Map<String, Object>> response = tournamentService.enterTournament(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(user);
        verify(tournamentGroupRepository, times(1)).save(group);
    }

    @Test
    public void testEnterTournament_UserAlreadyInTournament() {
        User user = new User();
        user.setInTournament(true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        ResponseEntity<Map<String, Object>> response = tournamentService.enterTournament(2L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, never()).save(user);
        verify(tournamentGroupRepository, never()).save(any());
    }

    @Test
    public void testEnterTournament_NotEnoughCoins() {
        User user = new User();
        user.setCoins(500); // Not enough coins
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        ResponseEntity<Map<String, Object>> response = tournamentService.enterTournament(3L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, never()).save(user);
        verify(tournamentGroupRepository, never()).save(any());
    }

    @Test
    public void testClaimTournamentReward_WithValidUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setInTournament(false);
        user.setTournamentRewardsClaimed(false);
        when(userRepository.findById(4L)).thenReturn(Optional.of(user));

        ResponseEntity<Map<String, Object>> response = tournamentService.claimTournamentReward(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testClaimTournamentReward_UserInTournament() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setInTournament(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Map<String, Object>> response = tournamentService.claimTournamentReward(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add additional assertions if needed
    }

    @Test
    public void testClaimTournamentReward_NoRewardsToClaim() {
        User user = new User();
        user.setInTournament(false);
        user.setTournamentRewardsClaimed(true);
        when(userRepository.findById(6L)).thenReturn(Optional.of(user));

        ResponseEntity<Map<String, Object>> response = tournamentService.claimTournamentReward(6L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, never()).save(user);
    }


}
