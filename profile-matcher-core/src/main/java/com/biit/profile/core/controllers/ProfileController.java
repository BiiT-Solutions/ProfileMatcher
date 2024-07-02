package com.biit.profile.core.controllers;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.profile.core.converters.ProfileConverter;
import com.biit.profile.core.converters.models.ProfileConverterRequest;
import com.biit.profile.core.exceptions.ProfileNotFoundException;
import com.biit.profile.core.kafka.ProfileEventSender;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProfileController extends KafkaElementController<Profile, Long, ProfileDTO, ProfileRepository,
        ProfileProvider, ProfileConverterRequest, ProfileConverter> {

    @Autowired
    protected ProfileController(ProfileProvider provider, ProfileConverter converter, ProfileEventSender eventSender) {
        super(provider, converter, eventSender);
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
}
