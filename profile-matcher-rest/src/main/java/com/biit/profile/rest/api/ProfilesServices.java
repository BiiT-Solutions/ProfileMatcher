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

import com.biit.profile.core.controllers.ProfileController;
import com.biit.profile.core.converters.ProfileConverter;
import com.biit.profile.core.converters.models.ProfileConverterRequest;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.server.rest.ElementServices;
import com.biit.usermanager.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
public class ProfilesServices extends ElementServices<Profile, Long, ProfileDTO, ProfileRepository,
        ProfileProvider, ProfileConverterRequest, ProfileConverter, ProfileController> {

    public ProfilesServices(ProfileController controller) {
        super(controller);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a profile by name.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/names/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO getByName(@Parameter(description = "Name of an existing profile", required = true) @PathVariable("name") String name,
                                Authentication authentication, HttpServletRequest request) {
        return getController().getByName(name);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a profile by name.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/types/{type}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfileDTO> getByType(@Parameter(description = "Type of an existing profiles", required = true) @PathVariable("type") String type,
                                      Authentication authentication, HttpServletRequest request) {
        return getController().getByType(type);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a profile by tracking code.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/tracking-codes/{trackingCode}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfileDTO> getByTrackingCode(@Parameter(description = "Tracking Code of an existing profiles", required = true)
                                              @PathVariable("trackingCode") String trackingCode,
                                              Authentication authentication, HttpServletRequest request) {
        return getController().getByTrackingCode(trackingCode);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all candidates from a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<UUID> getCandidates(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().getCandidates(id).stream().map(profileCandidate -> profileCandidate.getId().getUserUid())
                .collect(Collectors.toSet());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets all profiles from a candidate UUID.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = "/candidates/{uuid}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProfileDTO> getByCandidate(
            @Parameter(description = "Uuid from a user", required = true)
            @PathVariable("uuid") UUID uuid, Authentication authentication, HttpServletRequest httpRequest) {
        return getController().getByCandidateId(uuid);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Assigns a candidates to a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/{id}/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO addCandidates(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assign(id, users, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Assigns a candidate to a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/{id}/candidates/{userUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO addCandidatesByUUID(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "UUIDs from users", required = true)
            @PathVariable("userUUID") Collection<UUID> usersUUIDs,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assignByUUID(id, usersUUIDs, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Unassign candidates (removes) from a Profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/candidates/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO removeCandidates(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssign(id, users, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Unassign candidates (removes) from a Profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/{id}/candidates/{userUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO removeCandidatesByUUID(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "UUIDs from users", required = true)
            @PathVariable("userUUID") Collection<UUID> usersUUIDs,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssignByUUID(id, usersUUIDs, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a list of profiles by a selection of competences.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/competences/{threshold}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfileDTO> getByCompetences(
            @Parameter(name = "List of matching competences", required = false) @RequestParam(value = "competence", required = false) List<String> competences,
            @Parameter(description = "Minimum competence number to match", required = true) @PathVariable("threshold") int threshold,
            Authentication authentication, HttpServletRequest request) {
        return getController().findByCompetenceTagsIn(competences, threshold, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a list of profiles that matches a candidate.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/candidates/{candidateId}/thresholds/{threshold}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfileDTO> getByCandidate(
            @Parameter(description = "Candidate Profile Id", required = true) @PathVariable("candidateId") long candidateId,
            @Parameter(description = "Minimum competence number to match", required = true) @PathVariable("threshold") int threshold,
            Authentication authentication, HttpServletRequest request) {
        return getController().findByCandidate(candidateId, threshold, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets all profiles from a project.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = "/projects/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProfileDTO> getByProfile(
            @Parameter(description = "id", required = true)
            @PathVariable("id") Long id, Authentication authentication, HttpServletRequest httpRequest) {
        return getController().getByProjectId(id);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets all profiles from a user UUID.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = "/users/{uuid}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProfileDTO> getByUser(
            @Parameter(description = "Uuid from a user", required = true)
            @PathVariable("uuid") UUID uuid, Authentication authentication, HttpServletRequest httpRequest) {
        return getController().getByUserId(uuid);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Assign users to a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = "/{id}/users")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void assignToUsers(
            @Parameter(description = "id from the profile", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> userDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().assignUsers(id, userDTOS, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Unassign users to a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "/{id}/users/remove")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void unassignToUsers(
            @Parameter(description = "id from the profile", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> userDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().unassignUsers(id, userDTOS, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Assign profiles to a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = "/users/{id}/profiles")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void assignToUsers(
            @Parameter(description = "id", required = true)
            @PathVariable("id") UUID id,
            @RequestBody Collection<ProfileDTO> profileDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().assignProfiles(id, profileDTOS, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Assign profiles to a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "/users/{id}/profiles/remove")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void unassignToUsers(
            @Parameter(description = "id", required = true)
            @PathVariable("id") UUID id,
            @RequestBody Collection<ProfileDTO> profileDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().unassignProfiles(id, profileDTOS, authentication.getName());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all users from a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<UUID> getUsers(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().getUsers(id).stream().map(profileCandidate -> profileCandidate.getId().getUserUid())
                .collect(Collectors.toSet());
    }
}
