package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.UserProfile;
import com.biit.profile.persistence.entities.UserProfileId;
import com.biit.server.persistence.repositories.StorableObjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@Transactional
public interface UserProfileRepository extends StorableObjectRepository<UserProfile,
        UserProfileId> {

    Set<UserProfile> findByIdUserId(UUID userId);

    Set<UserProfile> findByIdUserIdAndIdProjectId(UUID userId, Long projectId);

    Set<UserProfile> findByIdProfileId(Long profileId);
}
