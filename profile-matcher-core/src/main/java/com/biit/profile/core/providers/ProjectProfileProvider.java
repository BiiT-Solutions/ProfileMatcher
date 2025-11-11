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
