package com.biit.profile.core.handlers;

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
