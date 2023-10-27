package com.example.game.controller;

import com.example.game.model.LeaderboardEntry;
import com.example.game.model.TournamentGroup;
import com.example.game.model.User;
import com.example.game.repository.UserRepository;
import com.example.game.service.UserService;
import com.example.game.service.LeaderboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderboardService leaderboardService;


    @PostMapping("/create/{username}")
    public ResponseEntity<Map<String, Object>> createUser(@PathVariable(required = true) String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User newUser = userService.createUser(username);
        if (newUser != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", newUser.getId());
            response.put("username", newUser.getUsername());
            response.put("level", newUser.getLevel());
            response.put("coins", newUser.getCoins());
            response.put("country", newUser.getCountry());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateLevel/{userId}")
    public ResponseEntity<Map<String, Object>> updateLevel(@PathVariable Long userId) {
        User user = userService.updateLevel(userId);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("coins", user.getCoins());
            response.put("level", user.getLevel());
            response.put("tournamentScore", user.getTournamentScore());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getGroupRank/{userId}")
    public ResponseEntity<Map<String, Object>> getGroupRank(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User with ID: " + userId + " does not exist."));
        }

        if (!user.isInTournament()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User with ID: " + userId + " is not in a tournament."));
        }

        // Get the user's tournament group
        TournamentGroup group = user.getTournamentGroup();

        if (group == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "An error occurred while fetching the user's tournament group."));
        }

        // Get the group leaderboard
        List<LeaderboardEntry> groupLeaderboard = leaderboardService.getGroupLeaderboard(group.getId());

        // Find the user's rank in the group leaderboard
        int userRank = findUserRankInLeaderboard(groupLeaderboard, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User with ID: " + userId + " is in tournament group " + group.getId());
        response.put("userRank", userRank);

        return ResponseEntity.ok(response);
    }

    private int findUserRankInLeaderboard(List<LeaderboardEntry> leaderboard, Long userId) {
        for (int i = 0; i < leaderboard.size(); i++) {
            LeaderboardEntry entry = leaderboard.get(i);
            if (entry.getUserId().equals(userId)) {
                return i + 1; // User's rank is 1-based, so add 1
            }
        }
        return -1; // User not found in the leaderboard
    }

}