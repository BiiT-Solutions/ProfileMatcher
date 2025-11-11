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
import com.biit.profile.core.providers.UserProjectProfileProvider;
import com.biit.profile.persistence.entities.Project;
import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.UserProjectProfile;
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
    private final UserProjectProfileProvider userProjectProfileProvider;

    protected ProjectController(ProjectProvider provider, ProjectConverter converter, IEventSender<ProjectDTO> eventSender,
                                List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider,
                                ProjectProfileProvider projectProfileProvider, UserProjectProfileProvider userProjectProfileProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.projectProfileProvider = projectProfileProvider;
        this.userProjectProfileProvider = userProjectProfileProvider;
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


        final Set<UserProjectProfile> existingProjectProfiles = userProjectProfileProvider.findByUserIdAndProjectId(userId, projectId);
        final List<Long> existingProfilesInProject = existingProjectProfiles.stream().map(p -> p.getId().getProfileId()).toList();
        final List<ProfileDTO> profilesToAdd = profilesDTOs.stream().filter(p -> !existingProfilesInProject.contains(p.getId())).toList();
        final List<UserProjectProfile> userProjectProfiles = new ArrayList<>();
        profilesToAdd.forEach(profile ->
                userProjectProfiles.add(new UserProjectProfile(userId, profile.getId(), projectId)));
        if (!userProjectProfiles.isEmpty()) {
            userProjectProfileProvider.saveAll(userProjectProfiles);
        }
    }

    public void unassignUsersFromProfiles(Long projectId, UUID userId, Collection<ProfileDTO> profilesDTOs, String creatorName) {
        final List<UserProjectProfile> userProjectProfiles = new ArrayList<>();
        profilesDTOs.forEach(profile ->
                userProjectProfiles.add(new UserProjectProfile(userId, profile.getId(), projectId)));
        if (!userProjectProfiles.isEmpty()) {
            userProjectProfileProvider.deleteAll(userProjectProfiles);
        }
    }


    public void assignUsersToProfiles(Long projectId, Long profileId, Collection<UserDTO> userDTOS, String creatorName) {
        projectProfileProvider.findByProjectIdAndProfileId(projectId, profileId).orElseThrow(() ->
                new ProfileNotAssignedToProjectException(this.getClass(), "Profile '" + profileId
                        + "' is not assigned to project '" + profileId + "'.")
        );

        final Set<UserProjectProfile> existingProjectProfiles = userProjectProfileProvider.findByProfileId(profileId);
        final List<UUID> existingUsersInProfile = existingProjectProfiles.stream().map(p -> p.getId().getUserUid()).toList();
        final List<UserDTO> profilesToAdd = userDTOS.stream().filter(u -> !existingUsersInProfile.contains(u.getUUID())).toList();
        final List<UserProjectProfile> userProjectProfiles = new ArrayList<>();
        profilesToAdd.forEach(user ->
                userProjectProfiles.add(new UserProjectProfile(user.getUUID(), profileId, projectId)));
        if (!userProjectProfiles.isEmpty()) {
            userProjectProfileProvider.saveAll(userProjectProfiles);
        }
    }

    public void unassignUsersFromProfiles(Long projectId, Long profileId, Collection<UserDTO> userDTOS, String creatorName) {
        final List<UserProjectProfile> userProjectProfiles = new ArrayList<>();
        userDTOS.forEach(user ->
                userProjectProfiles.add(new UserProjectProfile(user.getUUID(), profileId, projectId)));
        if (!userProjectProfiles.isEmpty()) {
            userProjectProfileProvider.deleteAll(userProjectProfiles);
        }
    }

    public Set<UUID> getUsers(Long projectId, Long profileId) {
        final Set<UserProjectProfile> existingProjectProfiles = userProjectProfileProvider.findByProjectIdAndProfileId(projectId, profileId);
        return existingProjectProfiles.stream().map(p -> p.getId().getUserUid()).collect(Collectors.toSet());
    }
}
