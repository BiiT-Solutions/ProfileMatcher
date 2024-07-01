package com.biit.profile.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.profile.core.converters.models.ProfileConverterRequest;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.persistence.entities.Profile;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverter extends ElementConverter<Profile, ProfileDTO, ProfileConverterRequest> {


    @Override
    protected ProfileDTO convertElement(ProfileConverterRequest from) {
        final ProfileDTO profileDTO = new ProfileDTO();
        BeanUtils.copyProperties(from.getEntity(), profileDTO);
        return profileDTO;
    }

    @Override
    public Profile reverse(ProfileDTO to) {
        if (to == null) {
            return null;
        }
        final Profile profile = new Profile();
        BeanUtils.copyProperties(to, profile);
        return profile;
    }
}
