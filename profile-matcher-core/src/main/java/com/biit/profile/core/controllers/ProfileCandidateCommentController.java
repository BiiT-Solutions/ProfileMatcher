package com.biit.profile.core.controllers;


import com.biit.profile.core.converters.ProfileCandidateCommentConverter;
import com.biit.profile.core.converters.models.ProfileCandidateCommentConverterRequest;
import com.biit.profile.core.models.ProfileCandidateCommentDTO;
import com.biit.profile.core.providers.ProfileCandidateCommentProvider;
import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateCommentRepository;
import com.biit.server.controller.ElementController;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
public class ProfileCandidateCommentController extends ElementController<ProfileCandidateComment, ProfileCandidateId, ProfileCandidateCommentDTO,
        ProfileCandidateCommentRepository, ProfileCandidateCommentProvider, ProfileCandidateCommentConverterRequest, ProfileCandidateCommentConverter> {

    protected ProfileCandidateCommentController(ProfileCandidateCommentProvider provider, ProfileCandidateCommentConverter converter) {
        super(provider, converter);
    }

    @Override
    protected ProfileCandidateCommentConverterRequest createConverterRequest(ProfileCandidateComment profileCandidateComment) {
        return new ProfileCandidateCommentConverterRequest(profileCandidateComment);
    }


    public ProfileCandidateComment getComment(Long profileId, Long userId) {
        //Delete previous one if exists.
        return getProvider().findByIdProfileIdAndIdUserId(profileId, userId);
    }


    public Set<ProfileCandidateComment> getComments(Long profileId) {
        //Delete previous one if exists.
        return getProvider().findByIdProfileId(profileId);
    }


    public ProfileCandidateComment addComment(Long profileId, Long userId, String comment) {
        //Delete previous one if exists.
        getProvider().deleteByIdProfileIdAndIdUserId(profileId, userId);

        return getProvider().save(new ProfileCandidateComment(profileId, userId, comment));
    }


    public void deleteComment(Long profileId, Long userId) {
        getProvider().deleteByIdProfileIdAndIdUserId(profileId, userId);
    }
}
