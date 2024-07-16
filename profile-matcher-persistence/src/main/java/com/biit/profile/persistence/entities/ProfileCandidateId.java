package com.biit.profile.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ProfileCandidateId implements Serializable {
    private static final int HASH_CODE = 31;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "user_uid", nullable = false)
    private UUID userUid;

    public ProfileCandidateId() {
        super();
    }

    public ProfileCandidateId(Long profileId, UUID userUid) {
        this.profileId = profileId;
        this.userUid = userUid;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public UUID getUserUid() {
        return userUid;
    }

    public void setUserUid(UUID userUid) {
        this.userUid = userUid;
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
        return Objects.equals(profileId, that.profileId) && Objects.equals(userUid, that.userUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, userUid);
    }
}
