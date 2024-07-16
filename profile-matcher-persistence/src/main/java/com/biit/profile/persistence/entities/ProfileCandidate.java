package com.biit.profile.persistence.entities;

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
