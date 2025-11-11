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

import java.io.Serial;
import java.util.UUID;


/***
 * Assigns a user to a profile.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "user_profiles", indexes = {
        @Index(name = "ind_users", columnList = "user_uid"),
        @Index(name = "ind_profiles", columnList = "profile_id"),
})
public class UserProfile extends StorableObject {

    @Serial
    private static final long serialVersionUID = 641413219070414764L;

    @EmbeddedId
    private UserProfileId id;

    public UserProfile() {
        super();
    }

    public UserProfile(UUID userId, Long profileId) {
        this();
        setId(new UserProfileId(userId, profileId));
    }

    public UserProfileId getId() {
        return id;
    }

    public void setId(UserProfileId id) {
        this.id = id;
    }
}
