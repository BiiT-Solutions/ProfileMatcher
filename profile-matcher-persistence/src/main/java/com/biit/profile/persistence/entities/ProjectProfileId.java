package com.biit.profile.persistence.entities;

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

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class ProjectProfileId implements Serializable {


    @Serial
    private static final long serialVersionUID = 5071100906763701815L;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    public ProjectProfileId() {
        super();
    }

    public ProjectProfileId(Long projectId, Long profileId) {
        this();
        this.projectId = projectId;
        this.profileId = profileId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}
