package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Transactional
public interface ProfileCandidateCommentRepository extends ElementRepository<ProfileCandidateComment,
        ProfileCandidateId> {

    Set<ProfileCandidateComment> findByIdProfileId(Long profileId);

    Set<ProfileCandidateComment> findByIdUserId(Long userId);

    ProfileCandidateComment findByIdProfileIdAndIdUserId(Long profileId, Long userId);

    void deleteByIdProfileIdAndIdUserId(Long profileId, Long userId);
}
