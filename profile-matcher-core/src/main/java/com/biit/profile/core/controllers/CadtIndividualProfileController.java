package com.biit.profile.core.controllers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.core.converters.CadtIndividualProfileConverter;
import com.biit.profile.core.converters.models.CadtIndividualProfileConverterRequest;
import com.biit.profile.core.models.CadtIndividualProfileDTO;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.biit.profile.core.providers.MetaviewerProvider;
import com.biit.profile.logger.ProfileLogger;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.profile.persistence.repositories.CadtIndividualProfileRepository;
import com.biit.server.controller.ElementController;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class CadtIndividualProfileController extends ElementController<CadtIndividualProfile, Long, CadtIndividualProfileDTO,
        CadtIndividualProfileRepository, CadtIndividualProfileProvider, CadtIndividualProfileConverterRequest, CadtIndividualProfileConverter> {

    private final MetaviewerProvider metaviewerProvider;

    protected CadtIndividualProfileController(CadtIndividualProfileProvider provider, CadtIndividualProfileConverter converter,
                                              MetaviewerProvider metaviewerProvider) {
        super(provider, converter);
        this.metaviewerProvider = metaviewerProvider;
    }

    @Override
    protected CadtIndividualProfileConverterRequest createConverterRequest(CadtIndividualProfile cadtIndividualProfile) {
        return new CadtIndividualProfileConverterRequest(cadtIndividualProfile);
    }


    public synchronized void newFormReceived(DroolsSubmittedForm droolsSubmittedForm, UUID session) {
        final CadtIndividualProfile cadtIndividualProfile = getProvider().create(droolsSubmittedForm, session);
        try {
            cadtIndividualProfile.validate();
            getProvider().save(cadtIndividualProfile);
            metaviewerProvider.newFormReceived(cadtIndividualProfile);
        } catch (InvalidProfileValueException e) {
            ProfileLogger.errorMessage(this.getClass(), e);
        }
    }
}
