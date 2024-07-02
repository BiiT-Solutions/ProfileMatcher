package com.biit.profile.rest.api;

import com.biit.profile.core.controllers.ProfileController;
import com.biit.profile.core.converters.ProfileConverter;
import com.biit.profile.core.converters.models.ProfileConverterRequest;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.server.rest.ElementServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles")
public class ProfileServices extends ElementServices<Profile, Long, ProfileDTO, ProfileRepository,
        ProfileProvider, ProfileConverterRequest, ProfileConverter, ProfileController> {

    public ProfileServices(ProfileController controller) {
        super(controller);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a profile by name.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping(value = {"/name/{name}"}, produces = {"application/json"})
    public ProfileDTO get(@Parameter(description = "Name of an existing profile", required = true) @PathVariable("name") String name,
                          Authentication authentication, HttpServletRequest request) {
        return getController().getByName(name);
    }
}
