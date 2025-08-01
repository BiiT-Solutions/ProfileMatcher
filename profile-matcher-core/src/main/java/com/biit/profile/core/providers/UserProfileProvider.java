package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.UserProfile;
import com.biit.profile.persistence.entities.UserProfileId;
import com.biit.profile.persistence.repositories.UserProfileRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserProfileProvider extends StorableObjectProvider<UserProfile, UserProfileId, UserProfileRepository> {


    public UserProfileProvider(UserProfileRepository repository) {
        super(repository);
    }


    public Set<UserProfile> findByUserId(UUID userId) {
        return getRepository().findByIdUserUid(userId);
    }


    public Set<UserProfile> findByProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public void deleteByProfileId(Long profileId) {
        getRepository().deleteByIdProfileId(profileId);
    }

}
