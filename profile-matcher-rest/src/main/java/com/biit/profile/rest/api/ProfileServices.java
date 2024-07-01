package com.biit.profile.rest.api;

import com.biit.profile.core.controllers.ProfileController;
import com.biit.profile.core.converters.ProfileConverter;
import com.biit.profile.core.converters.models.ProfileConverterRequest;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.server.rest.ElementServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles")
public class ProfileServices extends ElementServices<Profile, Long, ProfileDTO, ProfileRepository,
        ProfileProvider, ProfileConverterRequest, ProfileConverter, ProfileController> {

    public ProfileServices(ProfileController controller) {
        super(controller);
    }
}
