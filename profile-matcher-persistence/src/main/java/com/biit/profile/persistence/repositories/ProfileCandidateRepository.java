package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.ProfileCandidate;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.server.persistence.repositories.StorableObjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface ProfileCandidateRepository extends StorableObjectRepository<ProfileCandidate,
        ProfileCandidateId> {

    Set<ProfileCandidate> findByIdProfileId(Long profileId);

    Set<ProfileCandidate> findByIdUserId(Long userId);

    Optional<ProfileCandidate> findByIdProfileIdAndIdUserId(Long profileId, Long userId);
}
