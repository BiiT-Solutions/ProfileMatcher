package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.UserProjectProfile;
import com.biit.profile.persistence.entities.UserProjectProfileId;
import com.biit.profile.persistence.repositories.UserProjectProfileRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserProjectProfileProvider extends StorableObjectProvider<UserProjectProfile, UserProjectProfileId, UserProjectProfileRepository> {


    public UserProjectProfileProvider(UserProjectProfileRepository repository) {
        super(repository);
    }


    public Set<UserProjectProfile> findByUserId(UUID userId) {
        return getRepository().findByIdUserUid(userId);
    }

    public Set<UserProjectProfile> findByUserIdAndProjectId(UUID userId, Long projectId) {
        return getRepository().findByIdUserUidAndIdProjectId(userId, projectId);
    }

    public Set<UserProjectProfile> findByProjectIdAndProfileId(Long projectId, Long profileId) {
        return getRepository().findByIdProjectIdAndIdProfileId(projectId, profileId);
    }


    public Set<UserProjectProfile> findByProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }


    public void deleteByProjectIdAndProfileId(Long projectId, Long profileId) {
        getRepository().deleteByIdProjectIdAndIdProfileId(projectId, profileId);
    }

}
