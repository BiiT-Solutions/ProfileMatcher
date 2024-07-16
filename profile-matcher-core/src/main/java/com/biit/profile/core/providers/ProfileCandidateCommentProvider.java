package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateCommentRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ProfileCandidateCommentProvider extends ElementProvider<ProfileCandidateComment, ProfileCandidateId, ProfileCandidateCommentRepository> {


    @Autowired
    public ProfileCandidateCommentProvider(ProfileCandidateCommentRepository repository) {
        super(repository);
    }

    public Set<ProfileCandidateComment> findByIdUserUid(UUID userUid) {
        return getRepository().findByIdUserUid(userUid);
    }

    public Set<ProfileCandidateComment> findByIdProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public ProfileCandidateComment findByIdProfileIdAndIdUserUid(Long profileId, UUID userUid) {
        return getRepository().findByIdProfileIdAndIdUserUid(profileId, userUid);
    }

    public void deleteByIdProfileIdAndIdUserUid(Long profileId, UUID userUid) {
        getRepository().deleteByIdProfileIdAndIdUserUid(profileId, userUid);
    }
}
