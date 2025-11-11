package com.biit.profile.core.converters;

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

import com.biit.profile.core.converters.models.ProfileCandidateCommentConverterRequest;
import com.biit.profile.core.models.ProfileCandidateCommentDTO;
import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProfileCandidateCommentConverter extends ElementConverter<ProfileCandidateComment, ProfileCandidateCommentDTO,
        ProfileCandidateCommentConverterRequest> {


    @Override
    protected ProfileCandidateCommentDTO convertElement(ProfileCandidateCommentConverterRequest from) {
        final ProfileCandidateCommentDTO profileDTO = new ProfileCandidateCommentDTO();
        BeanUtils.copyProperties(from.getEntity(), profileDTO);
        return profileDTO;
    }

    @Override
    public ProfileCandidateComment reverse(ProfileCandidateCommentDTO to) {
        if (to == null) {
            return null;
        }
        final ProfileCandidateComment profile = new ProfileCandidateComment();
        BeanUtils.copyProperties(to, profile);
        return profile;
    }
}
