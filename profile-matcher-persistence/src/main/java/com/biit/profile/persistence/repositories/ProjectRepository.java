package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.Project;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProjectRepository extends ElementRepository<Project, Long> {

    Optional<Project> findByNameIgnoreCaseAndCreatedOnIgnoreCase(String name, String organization);

    Optional<Project> findByNameByHashAndCreatedOnHash(String name, String organization);

    long deleteByNameIgnoreCaseAndCreatedOnIgnoreCase(String name, String organization);

    long deleteByNameByHashAndCreatedOnHash(String name, String organization);
}
