package com.example.game.repository;

import com.example.game.model.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long> {
    List<TournamentGroup> findByGroupFullFalse();
    Optional<TournamentGroup> findById(Long id);

}
