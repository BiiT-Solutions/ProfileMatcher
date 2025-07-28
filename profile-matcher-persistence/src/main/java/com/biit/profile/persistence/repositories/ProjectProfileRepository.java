package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.ProjectProfileId;
import com.biit.server.persistence.repositories.StorableObjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Transactional
public interface ProjectProfileRepository extends StorableObjectRepository<ProjectProfile,
        ProjectProfileId> {

    Set<ProjectProfile> findByIdProjectId(Long projectId);

    Set<ProjectProfile> findByIdProfileId(Long profileId);
}
