package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.ProjectProfileId;
import com.biit.profile.persistence.repositories.ProjectProfileRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProjectProfileProvider extends StorableObjectProvider<ProjectProfile, ProjectProfileId, ProjectProfileRepository> {


    public ProjectProfileProvider(ProjectProfileRepository repository) {
        super(repository);
    }


    public Set<ProjectProfile> findByProjectId(Long projectId) {
        return getRepository().findByIdProjectId(projectId);
    }


    public Set<ProjectProfile> findByProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

}
