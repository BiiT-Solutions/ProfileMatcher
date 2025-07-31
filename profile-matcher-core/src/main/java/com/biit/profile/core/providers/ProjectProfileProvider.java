package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.ProjectProfileId;
import com.biit.profile.persistence.repositories.ProjectProfileRepository;
import com.biit.profile.persistence.repositories.UserProfileRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class ProjectProfileProvider extends StorableObjectProvider<ProjectProfile, ProjectProfileId, ProjectProfileRepository> {

    private final UserProfileRepository userProfileRepository;

    public ProjectProfileProvider(ProjectProfileRepository repository, UserProfileRepository userProfileRepository) {
        super(repository);
        this.userProfileRepository = userProfileRepository;
    }


    public Set<ProjectProfile> findByProjectId(Long projectId) {
        return getRepository().findByIdProjectId(projectId);
    }


    public Set<ProjectProfile> findByProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    @Override
    public void deleteAll(Collection<ProjectProfile> projectProfiles) {
        projectProfiles.forEach(p ->
                userProfileRepository.deleteByIdProjectIdAndIdProfileId(p.getId().getProjectId(), p.getId().getProfileId()));
        super.deleteAll(projectProfiles);
    }

}
