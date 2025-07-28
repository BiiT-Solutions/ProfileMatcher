package com.biit.profile.core.controllers;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.kafka.controllers.KafkaElementController;
import com.biit.profile.core.converters.ProfileConverter;
import com.biit.profile.core.converters.models.ProfileConverterRequest;
import com.biit.profile.core.exceptions.CandidateNotFoundException;
import com.biit.profile.core.exceptions.ProfileNotFoundException;
import com.biit.profile.core.kafka.ProfileEventSender;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.biit.profile.core.providers.ICadtController;
import com.biit.profile.core.providers.ProfileCandidateCommentProvider;
import com.biit.profile.core.providers.ProfileCandidateProvider;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.core.providers.ProjectProfileProvider;
import com.biit.profile.core.providers.UserProfileProvider;
import com.biit.profile.logger.ProfileLogger;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.entities.ProfileCandidate;
import com.biit.profile.persistence.entities.ProjectProfile;
import com.biit.profile.persistence.entities.UserProfile;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import com.biit.usermanager.dto.BasicUserDTO;
import com.biit.usermanager.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class ProfileController extends KafkaElementController<Profile, Long, ProfileDTO, ProfileRepository,
        ProfileProvider, ProfileConverterRequest, ProfileConverter> implements ICadtController {

    private final ProfileCandidateProvider profileCandidateProvider;
    private final ProfileCandidateCommentProvider profileCandidateCommentProvider;
    private final CadtIndividualProfileProvider cadtIndividualProfileProvider;
    private final ProjectProfileProvider projectProfileProvider;
    private final UserProfileProvider userProfileProvider;

    @Autowired
    protected ProfileController(ProfileProvider provider, ProfileConverter converter, ProfileEventSender eventSender,
                                ProfileCandidateProvider profileCandidateProvider,
                                ProfileCandidateCommentProvider profileCandidateCommentProvider,
                                CadtIndividualProfileProvider cadtIndividualProfileProvider,
                                List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider,
                                ProjectProfileProvider projectProfileProvider, UserProfileProvider userProfileProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.profileCandidateProvider = profileCandidateProvider;
        this.profileCandidateCommentProvider = profileCandidateCommentProvider;
        this.cadtIndividualProfileProvider = cadtIndividualProfileProvider;
        this.projectProfileProvider = projectProfileProvider;
        this.userProfileProvider = userProfileProvider;
    }

    @Override
    protected ProfileConverterRequest createConverterRequest(Profile profile) {
        return new ProfileConverterRequest(profile);
    }

    public ProfileDTO getByName(String name) {
        return convert(getProvider().findByName(name).orElseThrow(() ->
                new ProfileNotFoundException(this.getClass(),
                        "No Profile with name '" + name + "' found on the system.")));
    }

    public List<ProfileDTO> getByTrackingCode(String trackingCode) {
        return convertAll(getProvider().findByTrackingCode(trackingCode));
    }

    public List<ProfileDTO> getByType(String type) {
        return convertAll(getProvider().findByType(type));
    }

    public Set<ProfileCandidate> getCandidates(Long profileId) {
        return profileCandidateProvider.findByProfileId(profileId);
    }

    public ProfileDTO assignByUUID(Long profileId, Collection<UUID> userUUIDs, String assignedBy) {
        final Profile profile = getProvider().findById(profileId).orElseThrow(()
                -> new ProfileNotFoundException(this.getClass(), "No profile exists with id '" + profileId + "'."));

        final List<UUID> candidatesInProfile = profileCandidateProvider.findByProfileId(profileId).stream().map(profileCandidate ->
                profileCandidate.getId().getUserUid()).toList();

        userUUIDs = userUUIDs.stream().filter(userUUID -> !candidatesInProfile.contains(userUUID)).toList();

        //Store into the profile
        final List<ProfileCandidate> candidates = new ArrayList<>();
        userUUIDs.forEach(userUUID -> candidates.add(new ProfileCandidate(profileId, userUUID)));
        profileCandidateProvider.saveAll(candidates);

        profile.setUpdatedBy(assignedBy);

        return convert(getProvider().save(profile));
    }


    public ProfileDTO assign(Long profileId, Collection<UserDTO> users, String assignedBy) {
        return assignByUUID(profileId, users.stream().map(BasicUserDTO::getUUID).collect(Collectors.toSet()), assignedBy);
    }


    public ProfileDTO unAssign(Long profileId, Collection<UserDTO> users, String assignedBy) {
        return unAssignByUUID(profileId, users.stream().map(BasicUserDTO::getUUID).collect(Collectors.toSet()), assignedBy);
    }

    public ProfileDTO unAssignByUUID(Long profileId, Collection<UUID> userUUIDs, String assignedBy) {
        final Profile profile = getProvider().findById(profileId).orElseThrow(()
                -> new ProfileNotFoundException(this.getClass(), "No Profile exists with id '" + profileId + "'."));


        final List<ProfileCandidate> userGroupUsersToDelete = new ArrayList<>();
        userUUIDs.forEach(uuid -> userGroupUsersToDelete.add(new ProfileCandidate(profileId, uuid)));
        profileCandidateProvider.deleteAll(userGroupUsersToDelete);

        //Remove any comment.
        userGroupUsersToDelete.forEach(userGroupUserToDelete ->
                profileCandidateCommentProvider.deleteByIdProfileIdAndIdUserUid(userGroupUserToDelete.getId().getProfileId(),
                        userGroupUserToDelete.getId().getUserUid()));

        profile.setUpdatedBy(assignedBy);

        return convert(getProvider().save(profile));
    }

    public List<ProfileDTO> findByCompetencesIn(Collection<CadtCompetence> competences, int threshold, String searchedBy) {
        return findByCompetenceTagsIn(competences.stream().map(CadtCompetence::getTag).toList(), threshold, searchedBy);
    }


    public List<ProfileDTO> findByCompetenceTagsIn(Collection<String> competences, int threshold, String searchedBy) {
        ProfileLogger.debug(this.getClass(), "User '{}' is searching for profiles with '{}' competences.", searchedBy, competences);
        final List<ProfileDTO> matchingCompetences = convertAll(getProvider().findByCompetencesIn(competences, threshold));
        ProfileLogger.debug(this.getClass(), "Found '{}' profiles.", matchingCompetences.size());
        return matchingCompetences;
    }


    public List<ProfileDTO> findByCandidate(Long candidateProfileId, int threshold, String searchedBy) {
        final CadtIndividualProfile cadtIndividualProfile = cadtIndividualProfileProvider.findById(candidateProfileId)
                .orElseThrow(() -> new CandidateNotFoundException(this.getClass(), "No candidates with id '" + candidateProfileId + "' found!"));
        return findByCompetencesIn(cadtIndividualProfile.getSelectedCompetences(), threshold, searchedBy);
    }

    @Override
    public synchronized void newProfileReceived(DroolsSubmittedForm droolsSubmittedForm, UUID session) {
        final Profile profile = getProvider().create(droolsSubmittedForm, session);
        try {
            profile.validate();
            getProvider().save(profile);
        } catch (InvalidProfileValueException e) {
            ProfileLogger.errorMessage(this.getClass(), e);
        }
    }


    public List<ProfileDTO> getByProjectId(Long projectId) {
        final Set<ProjectProfile> projectProfiles = projectProfileProvider.findByProjectId(projectId);
        return convertAll(getProvider().findByIdIn(projectProfiles.stream().map(p -> p.getId().getProfileId())
                .collect(Collectors.toSet())));
    }


    public List<ProfileDTO> getByUserId(UUID userId) {
        final Set<UserProfile> projectProfiles = userProfileProvider.findByUserId(userId);
        return convertAll(getProvider().findByIdIn(projectProfiles.stream().map(p -> p.getId().getProfileId())
                .collect(Collectors.toSet())));
    }


    public void assignProfiles(UUID userId, Collection<ProfileDTO> profilesDTOs, String creatorName) {
        final Set<UserProfile> existingProjectProfiles = userProfileProvider.findByUserId(userId);
        final List<Long> existingProfilesInProject = existingProjectProfiles.stream().map(p -> p.getId().getProfileId()).toList();
        final List<ProfileDTO> profilesToAdd = profilesDTOs.stream().filter(p -> !existingProfilesInProject.contains(p.getId())).toList();
        final List<UserProfile> userProfiles = new ArrayList<>();
        profilesToAdd.forEach(profile ->
                userProfiles.add(new UserProfile(userId, profile.getId())));
        if (!userProfiles.isEmpty()) {
            userProfileProvider.saveAll(userProfiles);
        }
    }


    public void assignUsers(Long profileId, Collection<UserDTO> userDTOS, String creatorName) {
        final Set<UserProfile> existingProjectProfiles = userProfileProvider.findByProfileId(profileId);
        final List<UUID> existingUsersInProfile = existingProjectProfiles.stream().map(p -> p.getId().getUserId()).toList();
        final List<UserDTO> profilesToAdd = userDTOS.stream().filter(u -> !existingUsersInProfile.contains(u.getUUID())).toList();
        final List<UserProfile> userProfiles = new ArrayList<>();
        profilesToAdd.forEach(user ->
                userProfiles.add(new UserProfile(user.getUUID(), profileId)));
        if (!userProfiles.isEmpty()) {
            userProfileProvider.saveAll(userProfiles);
        }
    }
}
