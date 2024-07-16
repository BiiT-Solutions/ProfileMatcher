package com.biit.profile.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "profile_candidate_comments", indexes = {
        @Index(name = "ind_profile", columnList = "profile_id"),
        @Index(name = "ind_profile", columnList = "user_uid"),
})
public class ProfileCandidateComment extends Element<ProfileCandidateId> {
    public static final int COMMENT_LENGTH = 10000;

    @EmbeddedId
    private ProfileCandidateId id;


    @Column(name = "comment", length = COMMENT_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String comment = "";

    public ProfileCandidateComment() {
        super();
    }

    public ProfileCandidateComment(Long profileId, UUID userUid, String comment) {
        setId(new ProfileCandidateId(profileId, userUid));
        setComment(comment);
    }

    public ProfileCandidateId getId() {
        return id;
    }

    public void setId(ProfileCandidateId id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCandidateComment profileCandidate = (ProfileCandidateComment) o;
        return Objects.equals(id, profileCandidate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
