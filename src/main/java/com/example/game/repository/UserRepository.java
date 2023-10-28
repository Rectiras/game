package com.example.game.repository;

import com.example.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    @Query("SELECT u FROM User u WHERE u.inTournament = true")
    List<User> findAllInTournament(boolean b);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.availableTournament = false")
    void setAvailableTournamentToFalse();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.availableTournament = true")
    void setAvailableTournamentToTrue();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.inTournament = false")
    void setInTournamentToFalse();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.tournamentGroup = null")
    void clearTournamentGroup();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.tournamentScore = 0")
    void clearTournamentScore();
}
