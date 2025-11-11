package com.biit.profile.core.handlers;

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

import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.profile.core.controllers.CadtIndividualProfileController;
import com.biit.profile.persistence.entities.cadt.CadtArchetype;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.entities.cadt.CadtQuestion;
import com.biit.profile.persistence.entities.cadt.CardSelection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserCadtHandler extends CadtHandler {

    public UserCadtHandler(CadtIndividualProfileController cadtIndividualProfileController) {
        super(cadtIndividualProfileController);
    }


    public void assignArchetypes(CadtIndividualProfile cadtProfile, List<DroolsSubmittedQuestion> questions) {
        final List<CadtArchetype> selectedArchetypes = new ArrayList<>();
        for (DroolsSubmittedQuestion question : questions) {
            //Adding archetypes.
            final CadtArchetype archetype = CadtArchetype.fromTag(question.getAnswers().iterator().next());
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION1.getTag())) {
                assignCard(archetype, CardSelection.FIRST, cadtProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION2.getTag())) {
                assignCard(archetype, CardSelection.SHADOWED, cadtProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION3.getTag())) {
                assignCard(archetype, CardSelection.SECOND, cadtProfile);
                selectedArchetypes.add(archetype);
            }

            if (Objects.equals(question.getName(), CadtQuestion.QUESTION4.getTag())) {
                assignCard(archetype, CardSelection.FIRST, cadtProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION5.getTag())) {
                assignCard(archetype, CardSelection.SHADOWED, cadtProfile);
                selectedArchetypes.add(archetype);
            }
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION6.getTag())) {
                assignCard(archetype, CardSelection.SECOND, cadtProfile);
                selectedArchetypes.add(archetype);
            }
        }

        //Adding not selected archetypes.
        final List<CadtArchetype> archetypes = new ArrayList<>(List.of(CadtArchetype.values()));
        archetypes.removeAll(selectedArchetypes);

        for (CadtArchetype archetype : archetypes) {
            assignCard(archetype, CardSelection.DISCARDED, cadtProfile);
        }
    }


    private void assignCard(CadtArchetype archetype, CardSelection cardSelection, CadtIndividualProfile cadtProfile) {
        if (archetype != null) {
            cadtProfile.assignCard(archetype, cardSelection);
        }
    }


    public void assignCompetences(CadtIndividualProfile cadtProfile, List<DroolsSubmittedQuestion> questions) {
        for (DroolsSubmittedQuestion question : questions) {
            if (Objects.equals(question.getName(), CadtQuestion.COMPETENCES.getTag())) {
                for (String answer : question.getAnswers()) {
                    final CadtCompetence competence = CadtCompetence.fromTag(answer);
                    if (competence != null) {
                        cadtProfile.assignCompetence(competence);
                    }
                }
            }
        }
        //Defaults are already false.
    }

}
