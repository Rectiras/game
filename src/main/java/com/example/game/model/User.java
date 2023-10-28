package com.example.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Random;
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private int coins = 5000;
    private int level = 1;
    private final String country;
    private int tournamentScore = 0;
    private boolean inTournament = false;
    private boolean tournamentRewardsClaimed = true;

    private int tournamentReward = 0;

    private boolean availableTournament = true;

    @ManyToOne
    @JoinColumn(name = "tournament_group_id")
    @JsonIgnore // Ignore serialization of this field otherwise causes infinite loop because of referencing back and forth
    private TournamentGroup tournamentGroup;


    private static final List<String> allowedCountries = List.of("Turkey", "United States", "United Kingdom", "France", "Germany");

    public User() {
        Random rand = new Random();
        this.country = allowedCountries.get(rand.nextInt(allowedCountries.size()));
    }

    // Getters and setters for coins, level, and country
    public Long getId() {
        return id;
    }
    public String getUsername() { return username; }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTournamentScore() {
        return tournamentScore;
    }

    public void incrementTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }

    public boolean isInTournament() {
        return inTournament;
    }

    public void setInTournament(boolean inTournament) {
        this.inTournament = inTournament;
    }

    public boolean areTournamentRewardsClaimed() {
        return tournamentRewardsClaimed;
    }

    public void setTournamentRewardsClaimed(boolean tournamentRewardsClaimed) {
        this.tournamentRewardsClaimed = tournamentRewardsClaimed;
    }

    public String getCountry() {
        String country1 = country;
        return country1;
    }

    public void setTournamentGroup(TournamentGroup group) {
        this.tournamentGroup = group;
    }

    public TournamentGroup getTournamentGroup() {
        return tournamentGroup;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTournamentReward() {
        return tournamentReward;
    }

    public void setTournamentReward(int tournamentReward) {
        this.tournamentReward = tournamentReward;
    }

    public boolean isAvailableTournament() {
        return availableTournament;
    }

    public void setAvailableTournament(boolean availableTournament) {
        this.availableTournament = availableTournament;
    }
}
