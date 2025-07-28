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
            return getRepository().findByNameIgnoreCaseAndOrganizationIgnoreCase(name, organization);
        }
        return getRepository().findByNameByHashAndOrganizationByHash(name, organization);
    }


    public long deleteByNameAndOrganization(String name, String organization) {
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            return getRepository().deleteByNameIgnoreCaseAndOrganizationIgnoreCase(name, organization);
        }
        return getRepository().deleteByNameByHashAndOrganizationByHash(name, organization);
    }

}
