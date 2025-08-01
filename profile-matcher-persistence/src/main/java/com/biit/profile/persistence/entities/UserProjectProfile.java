package com.biit.profile.persistence.entities;

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
@Table(name = "user_project_profiles", indexes = {
        @Index(name = "ind_users", columnList = "user_uid"),
        @Index(name = "ind_profiles", columnList = "profile_id"),
        @Index(name = "ind_projects", columnList = "project_id"),
})
public class UserProjectProfile extends StorableObject {

    @Serial
    private static final long serialVersionUID = 641413219070414764L;

    @EmbeddedId
    private UserProjectProfileId id;

    public UserProjectProfile() {
        super();
    }

    public UserProjectProfile(UUID userId, Long profileId, Long projectId) {
        this();
        setId(new UserProjectProfileId(userId, profileId, projectId));
    }

    public UserProjectProfileId getId() {
        return id;
    }

    public void setId(UserProjectProfileId id) {
        this.id = id;
    }
}
