package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.ProjectProfileId;
import com.biit.server.persistence.repositories.StorableObjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface ProjectProfileRepository extends StorableObjectRepository<ProjectProfile,
        ProjectProfileId> {

    Optional<ProjectProfile> findByIdProjectIdAndIdProfileId(Long projectId, Long profileId);

    Set<ProjectProfile> findByIdProjectId(Long projectId);

    Set<ProjectProfile> findByIdProfileId(Long profileId);

    void deleteByIdProfileId(Long profileId);

    void deleteByIdProjectId(Long projectId);

    void deleteByIdProjectIdNotNull();

    void deleteByIdProfileIdNotNull();
}
