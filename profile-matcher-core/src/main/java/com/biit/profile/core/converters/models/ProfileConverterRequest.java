package com.biit.profile.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.profile.persistence.entities.Profile;

public class ProfileConverterRequest extends ConverterRequest<Profile> {
    public ProfileConverterRequest(Profile profile) {
        super(profile);
    }
}
