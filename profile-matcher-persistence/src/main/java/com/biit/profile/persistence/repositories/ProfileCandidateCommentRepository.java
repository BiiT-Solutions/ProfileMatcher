package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@Transactional
public interface ProfileCandidateCommentRepository extends ElementRepository<ProfileCandidateComment,
        ProfileCandidateId> {

    Set<ProfileCandidateComment> findByIdProfileId(Long profileId);

    Set<ProfileCandidateComment> findByIdUserUid(UUID userUid);

    ProfileCandidateComment findByIdProfileIdAndIdUserUid(Long profileId, UUID userUid);

    void deleteByIdProfileIdAndIdUserUid(Long profileId, UUID userUid);
}
