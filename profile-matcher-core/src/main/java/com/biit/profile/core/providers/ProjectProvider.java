package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.Project;
import com.biit.profile.persistence.repositories.ProjectProfileRepository;
import com.biit.profile.persistence.repositories.ProjectRepository;
import com.biit.profile.persistence.repositories.UserProjectProfileRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static com.biit.database.encryption.KeyProperty.getEncryptionKey;

@Service
public class ProjectProvider extends ElementProvider<Project, Long, ProjectRepository> {


    private final UserProjectProfileRepository userProjectProfileRepository;
    private final ProjectProfileRepository projectProfileRepository;

    protected ProjectProvider(ProjectRepository repository,
                              UserProjectProfileRepository userProjectProfileRepository,
                              ProjectProfileRepository projectProfileRepository) {
        super(repository);
        this.userProjectProfileRepository = userProjectProfileRepository;
        this.projectProfileRepository = projectProfileRepository;
    }


    public Optional<Project> findByNameAndOrganization(String name, String organization) {
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            return getRepository().findByNameIgnoreCaseAndCreatedOnIgnoreCase(name, organization);
        }
        return getRepository().findByNameByHashAndCreatedOnHash(name, organization);
    }


    public long deleteByNameAndOrganization(String name, String organization) {
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            return getRepository().deleteByNameIgnoreCaseAndCreatedOnIgnoreCase(name, organization);
        }
        return getRepository().deleteByNameByHashAndCreatedOnHash(name, organization);
    }


    @Override
    public void delete(Project entity) {
        projectProfileRepository.deleteByIdProjectId(entity.getId());
        userProjectProfileRepository.deleteByIdProjectId(entity.getId());
        super.delete(entity);
    }


    @Override
    public void deleteById(Long id) {
        projectProfileRepository.deleteByIdProjectId(id);
        userProjectProfileRepository.deleteByIdProjectId(id);
        super.deleteById(id);
    }


    @Override
    public void deleteAll() {
        projectProfileRepository.deleteByIdProjectIdNotNull();
        userProjectProfileRepository.deleteByIdProjectIdNotNull();
        super.deleteAll();
    }


    @Override
    public void deleteAll(Collection<Project> projects) {
        projects.forEach(p -> {
            projectProfileRepository.deleteByIdProjectId(p.getId());
            userProjectProfileRepository.deleteByIdProjectId(p.getId());
        });
        super.deleteAll(projects);
    }
}
