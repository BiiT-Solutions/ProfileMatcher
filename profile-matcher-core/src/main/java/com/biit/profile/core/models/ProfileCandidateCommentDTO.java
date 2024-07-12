package com.biit.profile.core.models;

import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.server.controllers.models.ElementDTO;

public class ProfileCandidateCommentDTO extends ElementDTO<ProfileCandidateId> {

    private ProfileCandidateId id;

    private String comment;

    public ProfileCandidateCommentDTO() {
        super();
    }

    public ProfileCandidateCommentDTO(String comment) {
        this();
        setComment(comment);
    }

    @Override
    public ProfileCandidateId getId() {
        return id;
    }

    @Override
    public void setId(ProfileCandidateId id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
