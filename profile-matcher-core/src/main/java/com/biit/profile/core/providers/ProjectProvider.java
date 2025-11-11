package com.biit.profile.core.providers;

/*-
 * #%L
 * Profile Matcher (Core)
 * %%
 * Copyright (C) 2024 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
