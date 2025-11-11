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

import com.biit.profile.persistence.entities.UserProfile;
import com.biit.profile.persistence.entities.UserProfileId;
import com.biit.profile.persistence.repositories.UserProfileRepository;
import com.biit.server.providers.StorableObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserProfileProvider extends StorableObjectProvider<UserProfile, UserProfileId, UserProfileRepository> {


    public UserProfileProvider(UserProfileRepository repository) {
        super(repository);
    }


    public Set<UserProfile> findByUserId(UUID userId) {
        return getRepository().findByIdUserUid(userId);
    }


    public Set<UserProfile> findByProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public void deleteByProfileId(Long profileId) {
        getRepository().deleteByIdProfileId(profileId);
    }

}
