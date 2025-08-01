package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.ProjectProfileId;
import com.biit.profile.persistence.repositories.ProjectProfileRepository;
import com.biit.profile.persistence.repositories.UserProjectProfileRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectProfileProvider extends StorableObjectProvider<ProjectProfile, ProjectProfileId, ProjectProfileRepository> {

    private final UserProjectProfileRepository userProjectProfileRepository;

    public ProjectProfileProvider(ProjectProfileRepository repository, UserProjectProfileRepository userProjectProfileRepository) {
        super(repository);
        this.userProjectProfileRepository = userProjectProfileRepository;
    }


    public Set<ProjectProfile> findByProjectId(Long projectId) {
        return getRepository().findByIdProjectId(projectId);
    }


    public Set<ProjectProfile> findByProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public Optional<ProjectProfile> findByProjectIdAndProfileId(Long projectId, Long profileId) {
        return getRepository().findByIdProjectIdAndIdProfileId(projectId, profileId);
    }

    @Override
    public void delete(ProjectProfile entity) {
        userProjectProfileRepository.deleteByIdProjectIdAndIdProfileId(entity.getId().getProjectId(), entity.getId().getProfileId());
        super.delete(entity);
    }

    @Override
    public void deleteById(ProjectProfileId id) {
        userProjectProfileRepository.deleteByIdProjectIdAndIdProfileId(id.getProjectId(), id.getProfileId());
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userProjectProfileRepository.deleteByIdProjectIdNotNull();
        super.deleteAll();
    }

    @Override
    public void deleteAll(Collection<ProjectProfile> projectProfiles) {
        projectProfiles.forEach(p ->
                userProjectProfileRepository.deleteByIdProjectIdAndIdProfileId(p.getId().getProjectId(), p.getId().getProfileId()));
        super.deleteAll(projectProfiles);
    }

}
