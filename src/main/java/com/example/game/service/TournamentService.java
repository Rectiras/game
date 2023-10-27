package com.example.game.service;

import com.example.game.model.LeaderboardEntry;
import com.example.game.model.TournamentGroup;
import com.example.game.model.User;
import com.example.game.repository.TournamentGroupRepository;
import com.example.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TournamentService {

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC") // This schedules the task to run at 00:00 UTC
    public void startTournament() {
        // Logic to start a new tournament
        System.out.println("Tournament started at 00:00 UTC.");

    }

    @Scheduled(cron = "0 0 20 * * *", zone = "UTC") // This schedules the task to run at 20:00 UTC
    public void endTournament() {
        // Logic to end the current tournament and prepare for the next one

        System.out.println("Tournament ended at 20:00 UTC.");

    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentGroupRepository tournamentGroupRepository;

    @Autowired
    private LeaderboardService leaderboardService; // Inject the LeaderboardService


    public ResponseEntity<Map<String, Object>> enterTournament(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User with ID: " + userId + " does not exist."));
        }

        if (user.getLevel() < 20) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User with ID: " + userId + " does not meet the level requirement."));
        }

        if (user.getCoins() < 1000) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User with ID: " + userId + " does not have enough coins to enter the tournament."));
        }

        if (!user.areTournamentRewardsClaimed()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User with ID: " + userId + " needs to claim their last tournament rewards."));
        }

        if (user.isInTournament()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User with ID: " + userId + " is already in a tournament."));
        }

        // Check if there is an available tournament group to join
        TournamentGroup group = findAvailableTournamentGroup(user.getCountry());

        try {
            if (group == null) {
                // Create a new tournament group since there is no available group
                group = new TournamentGroup();
                group.setGroupFull(false);
                group.setGroupMatchesStarted(false);
                group = tournamentGroupRepository.save(group);
            }

            // Add the user to the group
            user.setInTournament(true);
            user.setCoins(user.getCoins() - 1000);
            user.setTournamentGroup(group);
            userRepository.save(user);

            group.addMember(user);
            tournamentGroupRepository.save(group);

            // Retrieve and include the group leaderboard in the response
            List<LeaderboardEntry> groupLeaderboard = leaderboardService.getGroupLeaderboard(group.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User with ID: " + userId + " entered the tournament successfully!");
            response.put("groupLeaderboard", groupLeaderboard);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "An error occurred while entering the tournament."));
        }
    }

    // Find an available tournament group for a user's country
    private TournamentGroup findAvailableTournamentGroup(String country) {
        List<TournamentGroup> groups = tournamentGroupRepository.findByGroupFullFalse();

        for (TournamentGroup group : groups) {
            if (!group.isMemberFromCountry(country)) {
                return group;
            }
        }

        return null;
    }

    public String claimTournamentReward(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && !user.isInTournament() && !user.areTournamentRewardsClaimed()) {
            // Check if the user hasn't claimed rewards yet
            // Logic to determine and assign rewards based on user's ranking

            user.setTournamentRewardsClaimed(true);
            userRepository.save(user);
            return "Claimed tournament rewards successfully!";
        } else if (user != null && user.isInTournament()) {
            return "The tournament has to end for User with ID: " + userId + " before they can claim their rewards!";
        }
        return "You don't have any rewards to claim!";
    }

}
