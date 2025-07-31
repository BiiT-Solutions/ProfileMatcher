package com.biit.profile.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class UserProfileId implements Serializable {

    @Serial
    private static final long serialVersionUID = 3529300883056582198L;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    public UserProfileId() {
        super();
    }

    public UserProfileId(UUID userId, Long profileId, Long projectId) {
        this();
        this.userId = userId;
        this.profileId = profileId;
        this.projectId = projectId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}
