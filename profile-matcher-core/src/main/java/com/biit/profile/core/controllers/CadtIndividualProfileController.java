package com.biit.profile.core.controllers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.factmanager.client.FactClient;
import com.biit.factmanager.client.SearchParameters;
import com.biit.factmanager.dto.FactDTO;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CadtIndividualProfileController extends ElementController<CadtIndividualProfile, Long, CadtIndividualProfileDTO,
        CadtIndividualProfileRepository, CadtIndividualProfileProvider, CadtIndividualProfileConverterRequest, CadtIndividualProfileConverter> {

    private static final String FACT_SUBJECT = "CREATED";
    private static final String FACT_TYPE = "DroolsResultForm";
    private static final String FACT_APPLICATION = "BaseFormDroolsEngine";

    private final MetaviewerProvider metaviewerProvider;
    private final FactClient factClient;
    private final ObjectMapper objectMapper;

    protected CadtIndividualProfileController(CadtIndividualProfileProvider provider, CadtIndividualProfileConverter converter,
                                              MetaviewerProvider metaviewerProvider, FactClient factClient, ObjectMapper objectMapper) {
        super(provider, converter);
        this.metaviewerProvider = metaviewerProvider;
        this.factClient = factClient;
        this.objectMapper = objectMapper;
    }

    @Override
    protected CadtIndividualProfileConverterRequest createConverterRequest(CadtIndividualProfile cadtIndividualProfile) {
        return new CadtIndividualProfileConverterRequest(cadtIndividualProfile);
    }


    /**
     * Convert facts to database entities.
     */
    public void updateFromFactManager() {
        final Map<SearchParameters, Object> filter = new HashMap<>();
        filter.put(SearchParameters.ELEMENT_NAME, CadtIndividualProfileProvider.FORM_NAME);
        filter.put(SearchParameters.FACT_TYPE, FACT_TYPE);
        filter.put(SearchParameters.SUBJECT, FACT_SUBJECT);
        filter.put(SearchParameters.APPLICATION, FACT_APPLICATION);
        filter.put(SearchParameters.LATEST_BY_USER, true);

        final List<FactDTO> facts = factClient.get(filter, null);
        final List<CadtIndividualProfile> profiles = new ArrayList<>();
        for (final FactDTO factDTO : facts) {
            try {
                final DroolsSubmittedForm droolsSubmittedForm = objectMapper.readValue(factDTO.getValue(), DroolsSubmittedForm.class);
                profiles.add(getProvider().create(droolsSubmittedForm, factDTO.getSession() != null ? UUID.fromString(factDTO.getSession()) : null));
            } catch (JsonProcessingException e) {
                ProfileLogger.errorMessage(this.getClass(), e);
            }
        }
        getProvider().deleteAll();
        getProvider().saveAll(profiles);
        ProfileLogger.info(this.getClass(), "Stored '{}' profiles", profiles.size());
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
