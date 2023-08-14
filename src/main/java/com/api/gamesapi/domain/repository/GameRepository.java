package com.api.gamesapi.domain.repository;

import com.api.gamesapi.domain.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    boolean existsById(Long gameId);

    List<Game> findByCompanyId(Long companyId);
}
