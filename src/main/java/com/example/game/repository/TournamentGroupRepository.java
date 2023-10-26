package com.example.game.repository;

import com.example.game.model.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long> {
    List<TournamentGroup> findByGroupFullFalse();
}
