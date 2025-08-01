package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.UserProjectProfile;
import com.biit.profile.persistence.entities.UserProjectProfileId;
import com.biit.server.persistence.repositories.StorableObjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@Transactional
public interface UserProjectProfileRepository extends StorableObjectRepository<UserProjectProfile,
        UserProjectProfileId> {

    Set<UserProjectProfile> findByIdUserUid(UUID userId);

    Set<UserProjectProfile> findByIdUserUidAndIdProjectId(UUID userId, Long projectId);

    Set<UserProjectProfile> findByIdProjectIdAndIdProfileId(Long projectId, Long profileId);

    Set<UserProjectProfile> findByIdProfileId(Long profileId);

    void deleteByIdProjectIdAndIdProfileId(Long projectId, Long profileId);

    void deleteByIdProfileId(Long profileId);

    void deleteByIdProjectId(Long projectId);

    void deleteByIdProjectIdNotNull();

    void deleteByIdProfileIdNotNull();
}
