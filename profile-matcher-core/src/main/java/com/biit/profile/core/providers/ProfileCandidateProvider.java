package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.ProfileCandidate;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ProfileCandidateProvider extends StorableObjectProvider<ProfileCandidate, ProfileCandidateId, ProfileCandidateRepository> {


    @Autowired
    public ProfileCandidateProvider(ProfileCandidateRepository repository) {
        super(repository);
    }

    public Set<ProfileCandidate> findByUserUid(UUID userUid) {
        return getRepository().findByIdUserUid(userUid);
    }

    public Set<ProfileCandidate> findByProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public Optional<ProfileCandidate> findByProfileIdAndUserUid(Long profileId, UUID userUid) {
        return getRepository().findByIdProfileIdAndIdUserUid(profileId, userUid);
    }

    public ProfileCandidate assign(UUID userUid, Long teamId) {
        return save(new ProfileCandidate(teamId, userUid));
    }
}
