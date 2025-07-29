package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.Project;
import com.biit.profile.persistence.repositories.ProjectRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.biit.database.encryption.KeyProperty.getEncryptionKey;

@Service
public class ProjectProvider extends ElementProvider<Project, Long, ProjectRepository> {

    protected ProjectProvider(ProjectRepository repository) {
        super(repository);
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

}
