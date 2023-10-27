package com.example.game.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TournamentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tournamentGroup")
    private List<User> members = new ArrayList<>();

    private boolean groupFull;
    private boolean groupMatchesStarted;

    // Constructors, getters, and setters
    public boolean isGroupFull() {
        return groupFull;
    }

    public void setGroupFull(boolean groupFull) {
        this.groupFull = groupFull;
    }

    public boolean isGroupMatchesStarted() {
        return groupMatchesStarted;
    }

    public void setGroupMatchesStarted(boolean groupMatchesStarted) {
        this.groupMatchesStarted = groupMatchesStarted;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        members.add(user);
        if(isFull()){
            setGroupFull(true);
            setGroupMatchesStarted(true);
        }
    }

    public boolean isMemberFromCountry(String country) {
        for (User user : members) {
            if (user.getCountry().equals(country)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull() {
        return members.size() == 5;
    }

    public Long getId() {
        return id;
    }
}
