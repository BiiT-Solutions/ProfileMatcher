package com.biit.profile.rest.api;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
public class ProfileServices extends ElementServices<Profile, Long, ProfileDTO, ProfileRepository,
        ProfileProvider, ProfileConverterRequest, ProfileConverter, ProfileController> {

    public ProfileServices(ProfileController controller) {
        super(controller);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a profile by name.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/names/{name}"}, produces = {"application/json"})
    public ProfileDTO getByName(@Parameter(description = "Name of an existing profile", required = true) @PathVariable("name") String name,
                                Authentication authentication, HttpServletRequest request) {
        return getController().getByName(name);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a profile by name.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/types/{type}"}, produces = {"application/json"})
    public List<ProfileDTO> getByType(@Parameter(description = "Type of an existing profiles", required = true) @PathVariable("type") String type,
                                      Authentication authentication, HttpServletRequest request) {
        return getController().getByType(type);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a profile by tracking code.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/tracking-codes/{trackingCode}"}, produces = {"application/json"})
    public List<ProfileDTO> getByTrackingCode(@Parameter(description = "Tracking Code of an existing profiles", required = true)
                                              @PathVariable("trackingCode") String trackingCode,
                                              Authentication authentication, HttpServletRequest request) {
        return getController().getByTrackingCode(trackingCode);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all candidates from a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<Long> getCandidates(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().getCandidates(id).stream().map(profileCandidate -> profileCandidate.getId().getUserId())
                .collect(Collectors.toSet());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds candidates to a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO addCandidates(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assign(id, users, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Removes candidates from a Profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO removeCandidates(
            @Parameter(description = "Id of an existing Profile", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssign(id, users, authentication.getName());
    }
}
