package com.api.gamesapi.domain.repository;

import com.api.gamesapi.domain.model.Company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsById(Long companyId);

    List<Company> findByName(String name);

    Page<Company> findByNameContains(String name, Pageable pageable);

}
