package com.example.game.service;

import com.example.game.model.LeaderboardEntry;
import com.example.game.model.TournamentGroup;
import com.example.game.model.User;
import com.example.game.repository.TournamentGroupRepository;
import com.example.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LeaderboardService {
    @Autowired
    private TournamentGroupRepository tournamentGroupRepository;
    @Autowired
    private UserRepository userRepository;
    public List<LeaderboardEntry> getGroupLeaderboard(Long groupId) {
        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();

        // Retrieve the tournament group
        TournamentGroup group = tournamentGroupRepository.findById(groupId).orElse(null);

        if (group != null) {
            // Retrieve users in the group
            List<User> groupUsers = group.getMembers();

            // Sort the users by tournament score in descending order
            groupUsers.sort(Comparator.comparingInt(User::getTournamentScore).reversed());

            // Create LeaderboardEntry objects for each user
            int rank = 1;
            for (User user : groupUsers) {
                LeaderboardEntry entry = new LeaderboardEntry();
                entry.setUserId(user.getId());
                entry.setUsername(user.getUsername());
                entry.setCountry(user.getCountry());
                entry.setTournamentScore(user.getTournamentScore());
                entry.setRank(rank++);
                leaderboardEntries.add(entry);
            }
        }

        return leaderboardEntries;
    }
}

