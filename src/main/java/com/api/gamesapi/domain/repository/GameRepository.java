package com.api.gamesapi.domain.repository;

import com.api.gamesapi.domain.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    boolean existsById(Long gameId);

    Page<Game> findByCompanyId(Long companyId,Pageable pageable);
}
