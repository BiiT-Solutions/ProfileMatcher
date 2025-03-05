package com.biit.profile.core.controllers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.factmanager.client.FactClient;
import com.biit.factmanager.client.SearchParameters;
import com.biit.factmanager.dto.FactDTO;
import com.biit.profile.core.converters.CadtIndividualProfileConverter;
import com.biit.profile.core.converters.models.CadtIndividualProfileConverterRequest;
import com.biit.profile.core.exceptions.ProfileNotFoundException;
import com.biit.profile.core.models.CadtIndividualProfileDTO;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.biit.profile.core.providers.ICadtController;
import com.biit.profile.core.providers.MetaviewerProvider;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.logger.ProfileLogger;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.profile.persistence.repositories.CadtIndividualProfileRepository;
import com.biit.server.controller.ElementController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CadtIndividualProfileController extends ElementController<CadtIndividualProfile, Long, CadtIndividualProfileDTO,
        CadtIndividualProfileRepository, CadtIndividualProfileProvider, CadtIndividualProfileConverterRequest, CadtIndividualProfileConverter>
        implements ICadtController {

    private static final String FACT_SUBJECT = "CREATED";
    private static final String FACT_TYPE = "DroolsResultForm";
    private static final String FACT_APPLICATION = "BaseFormDroolsEngine";

    private final MetaviewerProvider metaviewerProvider;
    private final ProfileProvider profileProvider;
    private final FactClient factClient;
    private final ObjectMapper objectMapper;

    protected CadtIndividualProfileController(CadtIndividualProfileProvider provider, CadtIndividualProfileConverter converter,
                                              MetaviewerProvider metaviewerProvider, ProfileProvider profileProvider, FactClient factClient,
                                              ObjectMapper objectMapper) {
        super(provider, converter);
        this.metaviewerProvider = metaviewerProvider;
        this.profileProvider = profileProvider;
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


    @Override
    public synchronized void newProfileReceived(DroolsSubmittedForm droolsSubmittedForm, UUID session) {
        final CadtIndividualProfile cadtIndividualProfile = getProvider().create(droolsSubmittedForm, session);
        try {
            cadtIndividualProfile.validate();
            getProvider().save(cadtIndividualProfile);
            metaviewerProvider.newFormReceived(cadtIndividualProfile);
        } catch (InvalidProfileValueException e) {
            ProfileLogger.errorMessage(this.getClass(), e);
        }
    }


    public List<CadtIndividualProfileDTO> findByCompetences(Collection<CadtCompetence> competences, int threshold, String searchedBy) {
        return findByCompetenceTagsIn(competences.stream().map(CadtCompetence::getTag).toList(), threshold, searchedBy);
    }


    public List<CadtIndividualProfileDTO> findByCompetenceTagsIn(List<String> competences, int threshold, String searchedBy) {
        ProfileLogger.debug(this.getClass(), "User '{}' is searching for profiles with '{}' competences.", searchedBy, competences);
        final List<CadtIndividualProfileDTO> matchingCompetences = convertAll(getProvider().findByCompetencesIn(competences, threshold));
        ProfileLogger.debug(this.getClass(), "Found '{}' profiles.", matchingCompetences.size());
        return matchingCompetences;
    }

    public List<CadtIndividualProfileDTO> findByProfile(Long profileId, int threshold, String searchedBy) {
        final Profile profile = profileProvider.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(this.getClass(), "No profile with id '" + profileId + "' exists!"));
        return findByCompetences(profile.getSelectedCompetences(), threshold, searchedBy);
    }
}
