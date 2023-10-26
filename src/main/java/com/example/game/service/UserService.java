package com.example.game.service;

import com.example.game.model.User;
import com.example.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser() {
        User newUser = new User();
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
}