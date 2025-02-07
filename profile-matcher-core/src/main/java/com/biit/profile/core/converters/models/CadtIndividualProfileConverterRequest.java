package com.biit.profile.core.converters.models;

import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.server.converters.models.ConverterRequest;

public class CadtIndividualProfileConverterRequest extends ConverterRequest<CadtIndividualProfile> {
    public CadtIndividualProfileConverterRequest(CadtIndividualProfile profile) {
        super(profile);
    }
}
