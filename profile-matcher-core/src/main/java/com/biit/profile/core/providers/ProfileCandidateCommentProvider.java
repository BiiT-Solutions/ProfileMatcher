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

import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateCommentRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ProfileCandidateCommentProvider extends ElementProvider<ProfileCandidateComment, ProfileCandidateId, ProfileCandidateCommentRepository> {


    @Autowired
    public ProfileCandidateCommentProvider(ProfileCandidateCommentRepository repository) {
        super(repository);
    }

    public Set<ProfileCandidateComment> findByIdUserUid(UUID userUid) {
        return getRepository().findByIdUserUid(userUid);
    }

    public Set<ProfileCandidateComment> findByIdProfileId(Long profileId) {
        return getRepository().findByIdProfileId(profileId);
    }

    public ProfileCandidateComment findByIdProfileIdAndIdUserUid(Long profileId, UUID userUid) {
        return getRepository().findByIdProfileIdAndIdUserUid(profileId, userUid);
    }

    public void deleteByIdProfileIdAndIdUserUid(Long profileId, UUID userUid) {
        getRepository().deleteByIdProfileIdAndIdUserUid(profileId, userUid);
    }
}
