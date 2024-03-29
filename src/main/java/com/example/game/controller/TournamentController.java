package com.example.game.controller;

import com.example.game.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/enter/{userId}")
    public ResponseEntity<Map<String, Object>> enterTournament(@PathVariable Long userId) {
        return tournamentService.enterTournament(userId);
    }

    @PostMapping("/claimReward/{userId}")
    public ResponseEntity<Map<String, Object>> claimTournamentReward(@PathVariable Long userId) {
        return tournamentService.claimTournamentReward(userId);
    }

    // Additional endpoints for tournament-related actions
}
