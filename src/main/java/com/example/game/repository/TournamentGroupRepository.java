package com.example.game.repository;

import com.example.game.model.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long> {
    List<TournamentGroup> findByGroupFullFalse();
    Optional<TournamentGroup> findById(Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "TRUNCATE TABLE tournament_group")
    void truncateTable();

}
