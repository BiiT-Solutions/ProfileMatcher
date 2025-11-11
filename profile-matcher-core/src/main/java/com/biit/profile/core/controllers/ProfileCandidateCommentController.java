package com.biit.profile.core.controllers;

/*-
 * #%L
 * Profile Matcher (Core)
 * %%
 * Copyright (C) 2024 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.biit.profile.core.converters.ProfileCandidateCommentConverter;
import com.biit.profile.core.converters.models.ProfileCandidateCommentConverterRequest;
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

        return getProvider().save(new ProfileCandidateComment(profileId, userUid, comment));
    }


    public void deleteComment(Long profileId, UUID userUid) {
        getProvider().deleteByIdProfileIdAndIdUserUid(profileId, userUid);
    }
}
