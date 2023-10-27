package com.example.game.controller;

import com.example.game.model.LeaderboardEntry;
import com.example.game.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {
    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/group/{groupId}")
    public List<LeaderboardEntry> getGroupLeaderboard(@PathVariable Long groupId) {
        return leaderboardService.getGroupLeaderboard(groupId);
    }
}

