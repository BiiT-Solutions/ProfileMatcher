package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.ProfileCandidate;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProfileCandidateProvider extends StorableObjectProvider<ProfileCandidate, ProfileCandidateId, ProfileCandidateRepository> {


    @Autowired
    public ProfileCandidateProvider(ProfileCandidateRepository repository) {
        super(repository);
    }

    public Set<ProfileCandidate> findByIdUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public Set<ProfileCandidate> findByIdProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public ProfileCandidate assign(Long userId, Long teamId) {
        return save(new ProfileCandidate(teamId, userId));
    }
}
