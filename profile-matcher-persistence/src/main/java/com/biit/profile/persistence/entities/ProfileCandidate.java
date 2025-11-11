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

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;
import java.util.UUID;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "profile_candidates", indexes = {
        @Index(name = "ind_profile", columnList = "profile_id"),
})
public class ProfileCandidate extends StorableObject {

    @EmbeddedId
    private ProfileCandidateId id;

    public ProfileCandidate() {
        super();
    }

    public ProfileCandidate(Long profileId, UUID userUid) {
        setId(new ProfileCandidateId(profileId, userUid));
    }

    public ProfileCandidateId getId() {
        return id;
    }

    public void setId(ProfileCandidateId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCandidate profileCandidate = (ProfileCandidate) o;
        return Objects.equals(id, profileCandidate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
