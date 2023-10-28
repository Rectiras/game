package com.example.game.service;

import com.example.game.model.User;
import com.example.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(String username) {
        User newUser = new User();
        newUser.setUsername(username);

        // Adjust availableTournament based on UTC time
        boolean isTournamentAvailable = isTournamentAvailableNow();
        newUser.setAvailableTournament(isTournamentAvailable);

        return userRepository.save(newUser);
    }

    public User updateLevel(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setLevel(user.getLevel() + 1); // Increment level by 1
            user.setCoins(user.getCoins() + 25); // Add 25 coins

            // Check if the user is in a tournament group and group matches have started
            if (user.isInTournament() && user.getTournamentGroup().isGroupMatchesStarted()) {
                user.incrementTournamentScore(user.getTournamentScore() + 1);
            }

            return userRepository.save(user);
        }
        return null;
    }

    public boolean isTournamentAvailableNow() {
        // Get the current time in UTC
        Instant now = Instant.now();
        LocalTime localTime = now.atZone(ZoneOffset.UTC).toLocalTime();

        // Check if the time is between 20:00 and 00:00 UTC
        boolean isAvailable = localTime.isBefore(LocalTime.of(20, 0)) && localTime.isAfter(LocalTime.of(0, 0));

        return isAvailable;
    }
}