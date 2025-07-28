package com.biit.profile.persistence.entities;

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
