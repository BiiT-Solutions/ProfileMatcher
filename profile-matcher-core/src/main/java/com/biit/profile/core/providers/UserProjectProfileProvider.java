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
