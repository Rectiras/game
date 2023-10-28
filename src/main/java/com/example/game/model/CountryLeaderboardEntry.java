package com.example.game.model;

public class CountryLeaderboardEntry {
    private String countryName;
    private int totalTournamentScore;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getTotalTournamentScore() {
        return totalTournamentScore;
    }

    public void setTotalTournamentScore(int totalTournamentScore) {
        this.totalTournamentScore = totalTournamentScore;
    }
}
