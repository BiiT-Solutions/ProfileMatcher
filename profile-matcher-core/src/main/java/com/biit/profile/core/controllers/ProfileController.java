package com.biit.profile.core.controllers;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.profile.core.converters.ProfileConverter;
import com.biit.profile.core.converters.models.ProfileConverterRequest;
import com.biit.profile.core.exceptions.ProfileNotFoundException;
import com.biit.profile.core.kafka.ProfileEventSender;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.providers.ProfileCandidateCommentProvider;
import com.biit.profile.core.providers.ProfileCandidateProvider;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.entities.ProfileCandidate;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.usermanager.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
public class ProfileController extends KafkaElementController<Profile, Long, ProfileDTO, ProfileRepository,
        ProfileProvider, ProfileConverterRequest, ProfileConverter> {

    private final ProfileCandidateProvider profileCandidateProvider;
    private final ProfileCandidateCommentProvider profileCandidateCommentProvider;

    @Autowired
    protected ProfileController(ProfileProvider provider, ProfileConverter converter, ProfileEventSender eventSender,
                                ProfileCandidateProvider profileCandidateProvider,
                                ProfileCandidateCommentProvider profileCandidateCommentProvider) {
        super(provider, converter, eventSender);
        this.profileCandidateProvider = profileCandidateProvider;
        this.profileCandidateCommentProvider = profileCandidateCommentProvider;
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

    public ProfileDTO assign(Long profileId, Collection<UserDTO> users, String assignedBy) {
        final Profile profile = getProvider().findById(profileId).orElseThrow(()
                -> new ProfileNotFoundException(this.getClass(), "No profile exists with id '" + profileId + "'."));

        final List<Long> candidatesInProfile = profileCandidateProvider.findByProfileId(profileId).stream().map(profileCandidate ->
                profileCandidate.getId().getUserId()).toList();

        users = users.stream().filter(userDTO -> !candidatesInProfile.contains(userDTO.getId())).toList();

        //Store into the profile
        final List<ProfileCandidate> candidates = new ArrayList<>();
        users.forEach(userDTO -> candidates.add(new ProfileCandidate(profileId, userDTO.getId())));
        profileCandidateProvider.saveAll(candidates);

        profile.setUpdatedBy(assignedBy);

        return convert(getProvider().save(profile));
    }


    public ProfileDTO unAssign(Long profileId, Collection<UserDTO> users, String assignedBy) {
        final Profile profile = getProvider().findById(profileId).orElseThrow(()
                -> new ProfileNotFoundException(this.getClass(), "No Profile exists with id '" + profileId + "'."));


        final List<ProfileCandidate> userGroupUsersToDelete = new ArrayList<>();
        users.forEach(userDTO -> userGroupUsersToDelete.add(new ProfileCandidate(profileId, userDTO.getId())));
        profileCandidateProvider.deleteAll(userGroupUsersToDelete);

        //Remove any comment.
        userGroupUsersToDelete.forEach(userGroupUserToDelete ->
                profileCandidateCommentProvider.deleteByIdProfileIdAndIdUserId(userGroupUserToDelete.getId().getProfileId(),
                        userGroupUserToDelete.getId().getUserId()));

        profile.setUpdatedBy(assignedBy);

        return convert(getProvider().save(profile));
    }
}
