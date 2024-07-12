package com.biit.profile.core.converters.models;

import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.server.converters.models.ConverterRequest;

public class ProfileCandidateCommentConverterRequest extends ConverterRequest<ProfileCandidateComment> {
    public ProfileCandidateCommentConverterRequest(ProfileCandidateComment profile) {
        super(profile);
    }
}
