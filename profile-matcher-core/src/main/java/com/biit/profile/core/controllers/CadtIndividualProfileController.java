package com.biit.profile.core.controllers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.core.converters.CadtIndividualProfileConverter;
import com.biit.profile.core.converters.models.CadtIndividualProfileConverterRequest;
import com.biit.profile.core.models.CadtIndividualProfileDTO;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.repositories.CadtIndividualProfileRepository;
import com.biit.server.controller.ElementController;
import org.springframework.stereotype.Controller;

@Controller
public class CadtIndividualProfileController extends ElementController<CadtIndividualProfile, Long, CadtIndividualProfileDTO,
        CadtIndividualProfileRepository, CadtIndividualProfileProvider, CadtIndividualProfileConverterRequest, CadtIndividualProfileConverter> {

    protected CadtIndividualProfileController(CadtIndividualProfileProvider provider, CadtIndividualProfileConverter converter) {
        super(provider, converter);
    }

    @Override
    protected CadtIndividualProfileConverterRequest createConverterRequest(CadtIndividualProfile cadtIndividualProfile) {
        return null;
    }


    public synchronized void newFormReceived(DroolsSubmittedForm droolsSubmittedForm) {

    }
}
