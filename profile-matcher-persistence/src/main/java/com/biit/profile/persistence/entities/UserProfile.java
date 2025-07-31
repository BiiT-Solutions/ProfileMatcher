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
@Table(name = "user_profiles", indexes = {
        @Index(name = "ind_users", columnList = "user_id"),
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

    public UserProfile(UUID userId, Long profileId, Long projectId) {
        this();
        setId(new UserProfileId(userId, profileId, projectId));
    }

    public UserProfileId getId() {
        return id;
    }

    public void setId(UserProfileId id) {
        this.id = id;
    }
}
