package com.biit.profile.rest.api;

/*-
 * #%L
 * Profile Matcher (Rest)
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

import com.biit.profile.core.controllers.ProfileCandidateCommentController;
import com.biit.profile.core.converters.ProfileCandidateCommentConverter;
import com.biit.profile.core.converters.models.ProfileCandidateCommentConverterRequest;
import com.biit.profile.core.models.ProfileCandidateCommentDTO;
import com.biit.profile.core.providers.ProfileCandidateCommentProvider;
import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.ProfileCandidateId;
import com.biit.profile.persistence.repositories.ProfileCandidateCommentRepository;
import com.biit.server.rest.ElementServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/profiles-candidates-comments")
public class ProfilesCandidatesCommentsServices extends ElementServices<ProfileCandidateComment, ProfileCandidateId,
        ProfileCandidateCommentDTO, ProfileCandidateCommentRepository, ProfileCandidateCommentProvider,
        ProfileCandidateCommentConverterRequest, ProfileCandidateCommentConverter, ProfileCandidateCommentController> {

    public ProfilesCandidatesCommentsServices(ProfileCandidateCommentController controller) {
        super(controller);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a comment.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/profiles/{profileId}/users/{userUid}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileCandidateComment getByName(@Parameter(description = "Id of an existing profile", required = true) @PathVariable("profileId") Long profileId,
                                             @Parameter(description = "Id of an existing user", required = true) @PathVariable("userUid") UUID userUid,
                                             Authentication authentication, HttpServletRequest request) {
        return getController().getComment(profileId, userUid);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Deletes a comment.", security = {@SecurityRequirement(name = "bearerAuth")})
    @DeleteMapping(value = {"/profiles/{profileId}/users/{userUid}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteByName(@Parameter(description = "Id of an existing profile", required = true) @PathVariable("profileId") Long profileId,
                             @Parameter(description = "Id of an existing user", required = true) @PathVariable("userUid") UUID userUid,
                             Authentication authentication, HttpServletRequest request) {
        getController().deleteComment(profileId, userUid);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets all comments from a profile", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/profiles/{profileId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ProfileCandidateComment> getByType(@Parameter(description = "Id of an existing profile", required = true)
                                                  @PathVariable("profileId") Long profileId,
                                                  Authentication authentication, HttpServletRequest request) {
        return getController().getComments(profileId);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Adds a comment.", security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping(value = {"/profiles/{profileId}/users/{userUid}/comments"}, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.TEXT_PLAIN_VALUE)
    public ProfileCandidateComment putComment(@Parameter(description = "Id of an existing profile", required = true) @PathVariable("profileId") Long profileId,
                                              @Parameter(description = "UUID of an existing user", required = true) @PathVariable("userUid") UUID userUid,
                                              @RequestBody String comment, Authentication authentication, HttpServletRequest request) {
        return getController().addComment(profileId, userUid, comment);
    }

}
