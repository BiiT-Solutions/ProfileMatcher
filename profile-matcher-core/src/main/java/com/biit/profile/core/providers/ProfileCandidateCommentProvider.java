package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateCommentRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProfileCandidateCommentProvider extends ElementProvider<ProfileCandidateComment, ProfileCandidateId, ProfileCandidateCommentRepository> {


    @Autowired
    public ProfileCandidateCommentProvider(ProfileCandidateCommentRepository repository) {
        super(repository);
    }

    public Set<ProfileCandidateComment> findByIdUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public Set<ProfileCandidateComment> findByIdProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public ProfileCandidateComment findByIdProfileIdAndIdUserId(Long profileId, Long userId) {
        return getRepository().findByIdProfileIdAndIdUserId(profileId, userId);
    }

    public void deleteByIdProfileIdAndIdUserId(Long profileId, Long userId) {
        getRepository().deleteByIdProfileIdAndIdUserId(profileId, userId);
    }
}
