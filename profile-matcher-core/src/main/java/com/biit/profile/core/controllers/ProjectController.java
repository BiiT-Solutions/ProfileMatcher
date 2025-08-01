package com.biit.profile.core.controllers;

import com.biit.kafka.controllers.KafkaElementController;
import com.biit.kafka.events.IEventSender;
import com.biit.profile.core.converters.ProjectConverter;
import com.biit.profile.core.converters.models.ProjectConverterRequest;
import com.biit.profile.core.exceptions.ProfileNotAssignedToProjectException;
import com.biit.profile.core.exceptions.ProjectAlreadyExistsException;
import com.biit.profile.core.exceptions.ProjectNotFoundException;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.models.ProjectDTO;
import com.biit.profile.core.providers.ProjectProfileProvider;
import com.biit.profile.core.providers.ProjectProvider;
import com.biit.profile.core.providers.UserProfileProvider;
import com.biit.profile.persistence.entities.Project;
import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.UserProfile;
import com.biit.profile.persistence.repositories.ProjectRepository;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import com.biit.usermanager.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ProjectController extends KafkaElementController<Project, Long, ProjectDTO, ProjectRepository,
        ProjectProvider, ProjectConverterRequest, ProjectConverter> {

    private final ProjectProfileProvider projectProfileProvider;
    private final UserProfileProvider userProfileProvider;

    protected ProjectController(ProjectProvider provider, ProjectConverter converter, IEventSender<ProjectDTO> eventSender,
                                List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider,
                                ProjectProfileProvider projectProfileProvider, UserProfileProvider userProfileProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.projectProfileProvider = projectProfileProvider;
        this.userProfileProvider = userProfileProvider;
    }

    @Override
    protected ProjectConverterRequest createConverterRequest(Project project) {
        return new ProjectConverterRequest(project);
    }

    public ProjectDTO getByNameAndOrganization(String name, String organization) {
        return convert(getProvider().findByNameAndOrganization(name, organization).orElseThrow(
                () -> new ProjectNotFoundException(this.getClass(), "No project exists with name '" + name + "' on organization '" + organization + "'.")));
    }

    @Transactional
    public ProjectDTO create(ProjectDTO dto, String creatorName) {
        //Check if exists.
        if (getProvider().findByNameAndOrganization(dto.getName(), dto.getOrganization()).isPresent()) {
            throw new ProjectAlreadyExistsException(this.getClass(), "The project '" + dto.getName()
                    + "' already exists on organization '" + dto.getOrganization() + "'.");
        }
        return super.create(dto, creatorName);
    }

    public List<ProjectDTO> getByProfileId(Long profileId) {
        final Set<ProjectProfile> projectProfiles = projectProfileProvider.findByProfileId(profileId);
        return convertAll(getProvider().findByIdIn(projectProfiles.stream().map(p -> p.getId().getProjectId())
                .collect(Collectors.toSet())));
    }


    public void assignByProject(Long projectId, Collection<ProfileDTO> profilesDTOs, String creatorName) {
        final Set<ProjectProfile> existingProjectProfiles = projectProfileProvider.findByProjectId(projectId);
        final List<Long> existingProfilesInProject = existingProjectProfiles.stream().map(p -> p.getId().getProfileId()).toList();
        final List<ProfileDTO> profilesToAdd = profilesDTOs.stream().filter(p -> !existingProfilesInProject.contains(p.getId())).toList();
        final List<ProjectProfile> projectProfiles = new ArrayList<>();
        profilesToAdd.forEach(profile ->
                projectProfiles.add(new ProjectProfile(projectId, profile.getId())));
        if (!projectProfiles.isEmpty()) {
            projectProfileProvider.saveAll(projectProfiles);
        }
    }

    public void unassignByProject(Long projectId, Collection<ProfileDTO> profilesDTOs, String creatorName) {
        final List<ProjectProfile> projectProfiles = new ArrayList<>();
        profilesDTOs.forEach(profile ->
                projectProfiles.add(new ProjectProfile(projectId, profile.getId())));
        if (!projectProfiles.isEmpty()) {
            projectProfileProvider.deleteAll(projectProfiles);
        }
    }


    public void assignByProfile(Long profileId, Collection<ProjectDTO> projectDTOS, String creatorName) {
        final Set<ProjectProfile> existingProjectProfiles = projectProfileProvider.findByProfileId(profileId);
        final List<Long> existingProfilesInProject = existingProjectProfiles.stream().map(p -> p.getId().getProjectId()).toList();
        final List<ProjectDTO> projectsToAdd = projectDTOS.stream().filter(p -> !existingProfilesInProject.contains(p.getId())).toList();
        final List<ProjectProfile> projectProfiles = new ArrayList<>();
        projectsToAdd.forEach(project ->
                projectProfiles.add(new ProjectProfile(project.getId(), profileId)));
        if (!projectProfiles.isEmpty()) {
            projectProfileProvider.saveAll(projectProfiles);
        }
    }

    public void unassignByProfile(Long profileId, Collection<ProjectDTO> projectDTOS, String creatorName) {
        final List<ProjectProfile> projectProfiles = new ArrayList<>();
        projectDTOS.forEach(project ->
                projectProfiles.add(new ProjectProfile(project.getId(), profileId)));
        if (!projectProfiles.isEmpty()) {
            projectProfileProvider.deleteAll(projectProfiles);
        }
    }

    public void assignUsersToProfiles(Long projectId, UUID userId, Collection<ProfileDTO> profilesDTOs, String creatorName) {

        profilesDTOs.forEach(profile ->
                projectProfileProvider.findByProjectIdAndProfileId(projectId, profile.getId()).orElseThrow(() ->
                        new ProfileNotAssignedToProjectException(this.getClass(), "Profile '" + projectId
                                + "' is not assigned to project '" + profile.getId() + "'.")
                ));


        final Set<UserProfile> existingProjectProfiles = userProfileProvider.findByUserIdAndProjectId(userId, projectId);
        final List<Long> existingProfilesInProject = existingProjectProfiles.stream().map(p -> p.getId().getProfileId()).toList();
        final List<ProfileDTO> profilesToAdd = profilesDTOs.stream().filter(p -> !existingProfilesInProject.contains(p.getId())).toList();
        final List<UserProfile> userProfiles = new ArrayList<>();
        profilesToAdd.forEach(profile ->
                userProfiles.add(new UserProfile(userId, profile.getId(), projectId)));
        if (!userProfiles.isEmpty()) {
            userProfileProvider.saveAll(userProfiles);
        }
    }

    public void unassignUsersFromProfiles(Long projectId, UUID userId, Collection<ProfileDTO> profilesDTOs, String creatorName) {
        final List<UserProfile> userProfiles = new ArrayList<>();
        profilesDTOs.forEach(profile ->
                userProfiles.add(new UserProfile(userId, profile.getId(), projectId)));
        if (!userProfiles.isEmpty()) {
            userProfileProvider.deleteAll(userProfiles);
        }
    }


    public void assignUsersToProfiles(Long projectId, Long profileId, Collection<UserDTO> userDTOS, String creatorName) {
        projectProfileProvider.findByProjectIdAndProfileId(projectId, profileId).orElseThrow(() ->
                new ProfileNotAssignedToProjectException(this.getClass(), "Profile '" + profileId
                        + "' is not assigned to project '" + profileId + "'.")
        );

        final Set<UserProfile> existingProjectProfiles = userProfileProvider.findByProfileId(profileId);
        final List<UUID> existingUsersInProfile = existingProjectProfiles.stream().map(p -> p.getId().getUserUid()).toList();
        final List<UserDTO> profilesToAdd = userDTOS.stream().filter(u -> !existingUsersInProfile.contains(u.getUUID())).toList();
        final List<UserProfile> userProfiles = new ArrayList<>();
        profilesToAdd.forEach(user ->
                userProfiles.add(new UserProfile(user.getUUID(), profileId, projectId)));
        if (!userProfiles.isEmpty()) {
            userProfileProvider.saveAll(userProfiles);
        }
    }

    public void unassignUsersFromProfiles(Long projectId, Long profileId, Collection<UserDTO> userDTOS, String creatorName) {
        final List<UserProfile> userProfiles = new ArrayList<>();
        userDTOS.forEach(user ->
                userProfiles.add(new UserProfile(user.getUUID(), profileId, projectId)));
        if (!userProfiles.isEmpty()) {
            userProfileProvider.deleteAll(userProfiles);
        }
    }

    public Set<UUID> getUsers(Long projectId, Long profileId) {
        final Set<UserProfile> existingProjectProfiles = userProfileProvider.findByProjectIdAndProfileId(projectId, profileId);
        return existingProjectProfiles.stream().map(p -> p.getId().getUserUid()).collect(Collectors.toSet());
    }
}
