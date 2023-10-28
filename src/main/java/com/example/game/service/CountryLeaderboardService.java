package com.example.game.service;

import com.example.game.model.CountryLeaderboardEntry;
import com.example.game.model.User;
import com.example.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CountryLeaderboardService {
    @Autowired
    private UserRepository userRepository;

    public List<CountryLeaderboardEntry> getCountryLeaderboard() {
        List<CountryLeaderboardEntry> leaderboardEntries = new ArrayList<>();

        // Fetch all users participating in the tournament
        List<User> tournamentUsers = userRepository.findAllInTournament(true);

        // Create a map to aggregate scores by country
        Map<String, Integer> countryScores = new HashMap<>();

        // Aggregate scores for each user and country
        for (User user : tournamentUsers) {
            String country = user.getCountry();
            int userScore = user.getTournamentScore();

            // Add the user's score to the country's total score
            countryScores.put(country, countryScores.getOrDefault(country, 0) + userScore);
        }

        // Create leaderboard entries from aggregated scores
        for (Map.Entry<String, Integer> entry : countryScores.entrySet()) {
            CountryLeaderboardEntry leaderboardEntry = new CountryLeaderboardEntry();
            leaderboardEntry.setCountryName(entry.getKey());
            leaderboardEntry.setTotalTournamentScore(entry.getValue());
            leaderboardEntries.add(leaderboardEntry);
        }

        // Sort the leaderboard entries by total score in descending order
        leaderboardEntries.sort(Comparator.comparingInt(CountryLeaderboardEntry::getTotalTournamentScore).reversed());

        return leaderboardEntries;
    }
}


