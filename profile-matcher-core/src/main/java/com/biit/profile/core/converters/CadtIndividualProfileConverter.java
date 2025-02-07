package com.biit.profile.core.converters;

import com.biit.profile.core.converters.models.CadtIndividualProfileConverterRequest;
import com.biit.profile.core.models.CadtIndividualProfileDTO;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CadtIndividualProfileConverter extends ElementConverter<CadtIndividualProfile, CadtIndividualProfileDTO,
        CadtIndividualProfileConverterRequest> {


    @Override
    protected CadtIndividualProfileDTO convertElement(CadtIndividualProfileConverterRequest from) {
        final CadtIndividualProfileDTO profileDTO = new CadtIndividualProfileDTO();
        BeanUtils.copyProperties(from.getEntity(), profileDTO);
        profileDTO.setSession(from.getEntity().getSession());
        return profileDTO;
    }

    @Override
    public CadtIndividualProfile reverse(CadtIndividualProfileDTO from) {
        if (from == null) {
            return null;
        }
        final CadtIndividualProfile profile = new CadtIndividualProfile();
        BeanUtils.copyProperties(from, profile);
        profile.setSession(from.getSession());
        return profile;
    }
}
