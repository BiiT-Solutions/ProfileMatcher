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
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
public class ProfileCandidateCommentController extends ElementController<ProfileCandidateComment, ProfileCandidateId, ProfileCandidateCommentDTO,
        ProfileCandidateCommentRepository, ProfileCandidateCommentProvider, ProfileCandidateCommentConverterRequest, ProfileCandidateCommentConverter> {

    private final ProfileCandidateProvider profileCandidateProvider;

    protected ProfileCandidateCommentController(ProfileCandidateCommentProvider provider, ProfileCandidateCommentConverter converter,
                                                ProfileCandidateProvider profileCandidateProvider,
                                                List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, userOrganizationProvider);
        this.profileCandidateProvider = profileCandidateProvider;
    }

    @Override
    protected ProfileCandidateCommentConverterRequest createConverterRequest(ProfileCandidateComment profileCandidateComment) {
        return new ProfileCandidateCommentConverterRequest(profileCandidateComment);
    }


    public ProfileCandidateComment getComment(Long profileId, UUID userUid) {
        //Delete previous one if exists.
        return getProvider().findByIdProfileIdAndIdUserUid(profileId, userUid);
    }


    public Set<ProfileCandidateComment> getComments(Long profileId) {
        //Delete previous one if exists.
        return getProvider().findByIdProfileId(profileId);
    }


    public ProfileCandidateComment addComment(Long profileId, UUID userUid, String comment) {
        if (comment.length() > ProfileCandidateComment.COMMENT_LENGTH) {
            throw new CommentTooLongException(this.getClass(), "Comment length exceeds the limit of '"
                    + ProfileCandidateComment.COMMENT_LENGTH + "' bytes");
        }

        //Delete previous one if exists.
        getProvider().deleteByIdProfileIdAndIdUserUid(profileId, userUid);

        //Checks that exist the candidate
        if (profileCandidateProvider.findByProfileIdAndUserUid(profileId, userUid).isEmpty()) {
            throw new CandidateNotFoundException(this.getClass(), "No candidate '" + userUid + "' found for profile '" + profileId + "'.");
        }

        return getProvider().save(new ProfileCandidateComment(profileId, userUid, comment));
    }


    public void deleteComment(Long profileId, UUID userUid) {
        getProvider().deleteByIdProfileIdAndIdUserUid(profileId, userUid);
    }
}
