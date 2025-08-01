package com.biit.profile.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class UserProjectProfileId implements Serializable {

    @Serial
    private static final long serialVersionUID = 3529300883056582198L;

    @Column(name = "user_uid", nullable = false)
    private UUID userUid;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "project_id")
    private Long projectId;

    public UserProjectProfileId() {
        super();
    }

    public UserProjectProfileId(UUID userUid, Long profileId, Long projectId) {
        this();
        this.userUid = userUid;
        this.profileId = profileId;
        this.projectId = projectId;
    }

    public UUID getUserUid() {
        return userUid;
    }

    public void setUserUid(UUID userId) {
        this.userUid = userId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
