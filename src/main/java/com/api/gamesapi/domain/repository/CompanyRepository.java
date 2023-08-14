package com.api.gamesapi.domain.repository;

import com.api.gamesapi.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsById(Long companyId);
    List<Company> findByName(String name);

    List<Company> findByNameContains(String name);

}
