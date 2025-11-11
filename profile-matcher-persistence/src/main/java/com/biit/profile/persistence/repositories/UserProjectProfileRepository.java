package com.biit.profile.persistence.repositories;

/*-
 * #%L
 * Profile Matcher (Persistence)
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
import com.biit.server.persistence.repositories.StorableObjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@Transactional
public interface UserProjectProfileRepository extends StorableObjectRepository<UserProjectProfile,
        UserProjectProfileId> {

    Set<UserProjectProfile> findByIdUserUid(UUID userId);

    Set<UserProjectProfile> findByIdUserUidAndIdProjectId(UUID userId, Long projectId);

    Set<UserProjectProfile> findByIdProjectIdAndIdProfileId(Long projectId, Long profileId);

    Set<UserProjectProfile> findByIdProfileId(Long profileId);

    void deleteByIdProjectIdAndIdProfileId(Long projectId, Long profileId);

    void deleteByIdProfileId(Long profileId);

    void deleteByIdProjectId(Long projectId);

    void deleteByIdProjectIdNotNull();

    void deleteByIdProfileIdNotNull();
}
