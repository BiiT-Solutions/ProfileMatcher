package com.biit.profile.core.models;

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

import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.server.controllers.models.ElementDTO;

public class ProfileCandidateCommentDTO extends ElementDTO<ProfileCandidateId> {

    private ProfileCandidateId id;

    private String comment;

    public ProfileCandidateCommentDTO() {
        super();
    }

    public ProfileCandidateCommentDTO(String comment) {
        this();
        setComment(comment);
    }

    @Override
    public ProfileCandidateId getId() {
        return id;
    }

    @Override
    public void setId(ProfileCandidateId id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
