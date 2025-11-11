package com.biit.profile.core.providers;

/*-
 * #%L
 * Profile Matcher (Core)
 * %%
 * Copyright (C) 2024 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.profile.core.exceptions.InvalidFormException;
import com.biit.profile.persistence.entities.cadt.CadtArchetype;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.entities.cadt.CadtQuestion;
import com.biit.profile.persistence.entities.cadt.CardSelection;
import com.biit.profile.persistence.repositories.CadtIndividualProfileRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class CadtIndividualProfileProvider extends ElementProvider<CadtIndividualProfile, Long, CadtIndividualProfileRepository> {

    public static final String FORM_NAME = "CADT_Score";

    private static final String FORM_SCORE_VARIABLE = "Score";

    public CadtIndividualProfileProvider(CadtIndividualProfileRepository repository) {
        super(repository);
    }

    public CadtIndividualProfile create(DroolsSubmittedForm droolsSubmittedForm, UUID session) {
        if (!Objects.equals(droolsSubmittedForm.getName(), FORM_NAME)) {
            throw new InvalidFormException("Form '" + droolsSubmittedForm.getName() + "' is not the correct form.");
        }
        final CadtIndividualProfile cadtIndividualProfile = new CadtIndividualProfile();
        cadtIndividualProfile.setCreatedBy(droolsSubmittedForm.getSubmittedBy());
        cadtIndividualProfile.setCreatedAt(droolsSubmittedForm.getSubmittedAt());
        cadtIndividualProfile.setDroolsId(droolsSubmittedForm.getId());
        cadtIndividualProfile.setFormVersion(droolsSubmittedForm.getVersion());
        cadtIndividualProfile.setSession(session);
        assignArchetypes(cadtIndividualProfile, droolsSubmittedForm.getChildrenRecursive(DroolsSubmittedQuestion.class));
        assignCompetences(cadtIndividualProfile, droolsSubmittedForm.getChildrenRecursive(DroolsSubmittedQuestion.class));
        assignScore(cadtIndividualProfile, droolsSubmittedForm.getFormVariables().get("/DroolsSubmittedForm[@label='" + FORM_NAME + "']"));
        return cadtIndividualProfile;
    }

    private void assignArchetypes(CadtIndividualProfile cadtIndividualProfile, List<DroolsSubmittedQuestion> questions) {
        final List<CadtArchetype> selectedArchetypes = new ArrayList<>();
        for (DroolsSubmittedQuestion question : questions) {
            //Adding archetypes.
            final CadtArchetype archetype = CadtArchetype.fromTag(question.getAnswers().iterator().next());
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION1.getTag())) {
                assignCard(archetype, CardSelection.FIRST, cadtIndividualProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION2.getTag())) {
                assignCard(archetype, CardSelection.SHADOWED, cadtIndividualProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION3.getTag())) {
                assignCard(archetype, CardSelection.SECOND, cadtIndividualProfile);
                selectedArchetypes.add(archetype);
            }

            if (Objects.equals(question.getName(), CadtQuestion.QUESTION4.getTag())) {
                assignCard(archetype, CardSelection.FIRST, cadtIndividualProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION5.getTag())) {
                assignCard(archetype, CardSelection.SHADOWED, cadtIndividualProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION6.getTag())) {
                assignCard(archetype, CardSelection.SECOND, cadtIndividualProfile);
                selectedArchetypes.add(archetype);
            }
        }

        //Adding not selected archetypes.
        final List<CadtArchetype> archetypes = new ArrayList<>(List.of(CadtArchetype.values()));
        archetypes.removeAll(selectedArchetypes);

        for (CadtArchetype archetype : archetypes) {
            assignCard(archetype, CardSelection.DISCARDED, cadtIndividualProfile);
        }
    }

    private void assignCard(CadtArchetype archetype, CardSelection cardSelection, CadtIndividualProfile cadtIndividualProfile) {
        if (archetype != null) {
            cadtIndividualProfile.assignCard(archetype, cardSelection);
        }
    }

    private void assignCompetences(CadtIndividualProfile cadtIndividualProfile, List<DroolsSubmittedQuestion> questions) {
        for (DroolsSubmittedQuestion question : questions) {
            if (Objects.equals(question.getName(), CadtQuestion.COMPETENCES.getTag())) {
                for (String answer : question.getAnswers()) {
                    final CadtCompetence competence = CadtCompetence.fromTag(answer);
                    if (competence != null) {
                        cadtIndividualProfile.assignCompetence(competence);
                    }
                }
            }
        }
        //Defaults are already false.
    }

    private void assignScore(CadtIndividualProfile cadtIndividualProfile, Map<String, Object> formVariables) {
        cadtIndividualProfile.setScore((double) formVariables.get(FORM_SCORE_VARIABLE));
    }


    public List<CadtIndividualProfile> findByCompetencesIn(Collection<String> competences, int threshold) {
        return getRepository().findByCompetencesIn(competences, threshold);
    }


}
