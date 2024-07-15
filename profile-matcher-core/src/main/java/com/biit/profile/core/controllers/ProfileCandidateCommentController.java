package com.biit.profile.core.controllers;


import com.biit.profile.core.converters.ProfileCandidateCommentConverter;
import com.biit.profile.core.converters.models.ProfileCandidateCommentConverterRequest;
import com.biit.profile.core.exceptions.CandidateNotFoundException;
import com.biit.profile.core.exceptions.CommentTooLongException;
import com.biit.profile.core.models.ProfileCandidateCommentDTO;
import com.biit.profile.core.providers.ProfileCandidateCommentProvider;
import com.biit.profile.core.providers.ProfileCandidateProvider;
import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateCommentRepository;
import com.biit.server.controller.ElementController;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
public class ProfileCandidateCommentController extends ElementController<ProfileCandidateComment, ProfileCandidateId, ProfileCandidateCommentDTO,
        ProfileCandidateCommentRepository, ProfileCandidateCommentProvider, ProfileCandidateCommentConverterRequest, ProfileCandidateCommentConverter> {

    private final ProfileCandidateProvider profileCandidateProvider;

    protected ProfileCandidateCommentController(ProfileCandidateCommentProvider provider, ProfileCandidateCommentConverter converter,
                                                ProfileCandidateProvider profileCandidateProvider) {
        super(provider, converter);
        this.profileCandidateProvider = profileCandidateProvider;
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
        if (comment.length() > ProfileCandidateComment.COMMENT_LENGTH) {
            throw new CommentTooLongException(this.getClass(), "Comment length exceeds the limit of '"
                    + ProfileCandidateComment.COMMENT_LENGTH + "' bytes");
        }

        //Delete previous one if exists.
        getProvider().deleteByIdProfileIdAndIdUserId(profileId, userId);

        //Checks that exist the candidate
        profileCandidateProvider.findByProfileIdAndUserId(profileId, userId).orElseThrow(() ->
                new CandidateNotFoundException(this.getClass(), "No candidate '" + userId + "' found for profile '" + profileId + "'."));

        return getProvider().save(new ProfileCandidateComment(profileId, userId, comment));
    }


    public void deleteComment(Long profileId, Long userId) {
        getProvider().deleteByIdProfileIdAndIdUserId(profileId, userId);
    }
}
