package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.Project;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProjectRepository extends ElementRepository<Project, Long> {

    Optional<Project> findByNameIgnoreCaseAndOrganizationIgnoreCase(String name, String organization);

    Optional<Project> findByNameByHashAndOrganizationByHash(String name, String organization);

    long deleteByNameIgnoreCaseAndOrganizationIgnoreCase(String name, String organization);

    long deleteByNameByHashAndOrganizationByHash(String name, String organization);
}
