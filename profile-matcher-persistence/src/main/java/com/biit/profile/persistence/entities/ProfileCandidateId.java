package com.biit.profile.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProfileCandidateId implements Serializable {
    private static final int HASH_CODE = 31;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public ProfileCandidateId() {
        super();
    }

    public ProfileCandidateId(Long profileId, Long userId) {
        this.profileId = profileId;
        this.userId = userId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCandidateId that = (ProfileCandidateId) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, userId);
    }
}
