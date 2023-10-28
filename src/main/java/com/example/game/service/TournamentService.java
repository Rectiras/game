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
        userRepository.setAvailableTournamentToTrue();

        System.out.println("A new tournament started at 00:00 UTC.");

    }

    @Scheduled(cron = "0 0 20 * * *", zone = "UTC") // This schedules the task to run at 20:00 UTC
    public void endTournament() {
        // Logic to end the current tournament and prepare for the next one
        endTournamentDaily();

        System.out.println("The tournament ended at 20:00 UTC.");

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

        if (!user.isAvailableTournament()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "There is no available tournament at the moment, please enter when the new one begins."));
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

    public ResponseEntity<Map<String, Object>> claimTournamentReward(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Map<String, Object> response = new HashMap<>();

        if (user != null && !user.isInTournament() && !user.areTournamentRewardsClaimed()) {
            // Check if the user hasn't claimed rewards yet
            // Logic to determine and assign rewards based on user's ranking

            user.setTournamentRewardsClaimed(true);
            int tournamentReward = user.getTournamentReward();
            user.setTournamentReward(0);
            user.setCoins(user.getCoins() + tournamentReward);
            userRepository.save(user);

            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("coins", user.getCoins());
            response.put("message", "Claimed tournament rewards successfully!");
        } else if (user != null && user.isInTournament()) {
            response.put("message", "The tournament has to end for User with ID: " + userId + " before they can claim their rewards!");
        } else {
            response.put("message", "You don't have any rewards to claim!");
        }

        return ResponseEntity.ok(response);
    }


    public void endTournamentDaily() {
        // Set available_tournament to false for all users
        userRepository.setAvailableTournamentToFalse();

        // Set in_tournament to 0 for all users
        userRepository.setInTournamentToFalse();

        // Get all tournament groups
        List<TournamentGroup> tournamentGroups = tournamentGroupRepository.findAll();

        for (TournamentGroup group : tournamentGroups) {
            // Get the group leaderboard
            List<LeaderboardEntry> groupLeaderboard = leaderboardService.getGroupLeaderboard(group.getId());

            // Check if there are at least two users in the group
            if (groupLeaderboard.size() >= 2) {
                // Update rewards for the top two users
                User firstPlaceUser = userRepository.findById(groupLeaderboard.get(0).getUserId()).orElse(null);
                User secondPlaceUser = userRepository.findById(groupLeaderboard.get(1).getUserId()).orElse(null);

                if (firstPlaceUser != null) {
                    firstPlaceUser.setTournamentReward(10000);
                    firstPlaceUser.setTournamentRewardsClaimed(false);
                    userRepository.save(firstPlaceUser);
                }

                if (secondPlaceUser != null) {
                    secondPlaceUser.setTournamentReward(5000);
                    secondPlaceUser.setTournamentRewardsClaimed(false);
                    userRepository.save(secondPlaceUser);
                }
            }
        }
        // Set tournament_group_id to null for all users
        userRepository.clearTournamentGroup();

        // Set tournament_score to 0 for all users
        userRepository.clearTournamentScore();

        // Truncate the tournament_group table
        tournamentGroupRepository.truncateTable();

    }

}
