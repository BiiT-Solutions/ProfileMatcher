package com.biit.profile.rest.api;

import com.biit.profile.core.controllers.ProjectController;
import com.biit.profile.core.converters.ProjectConverter;
import com.biit.profile.core.converters.models.ProjectConverterRequest;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.models.ProjectDTO;
import com.biit.profile.core.providers.ProjectProvider;
import com.biit.profile.persistence.entities.Project;
import com.biit.profile.persistence.repositories.ProjectRepository;
import com.biit.server.rest.ElementServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectServices extends ElementServices<Project, Long, ProjectDTO, ProjectRepository, ProjectProvider,
        ProjectConverterRequest, ProjectConverter, ProjectController> {


    protected ProjectServices(ProjectController controller) {
        super(controller);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets all projects from a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = "/profiles/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public List<ProjectDTO> getByProfile(
            @Parameter(description = "id", required = true)
            @PathVariable("id") Long id, Authentication authentication, HttpServletRequest httpRequest) {
        return getController().getByProfileId(id);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Assigns a collection of profiles to a project.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void assignTo(
            @Parameter(description = "id", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<ProfileDTO> profileDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().assignByProject(id, profileDTOS, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Unassigns a collection of profiles to a project.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "/{id}/profiles/remove")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void unassignFrom(
            @Parameter(description = "id", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<ProfileDTO> profileDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().unassignByProject(id, profileDTOS, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Assigns a collection of projects to a profile.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = "/profiles/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void assignByProfile(
            @Parameter(description = "Profile id", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<ProjectDTO> projectDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().assignByProfile(id, projectDTOS, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + " @securityService.organizationAdminPrivilege)")
    @Operation(summary = "Unassigns a collection of profiles to a project.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "/profiles/{id}/remove")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void unassignFromProfile(
            @Parameter(description = "id", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<ProjectDTO> projectDTOS,
            Authentication authentication, HttpServletRequest httpRequest) {
        getController().unassignByProfile(id, projectDTOS, authentication.getName());
    }
}
