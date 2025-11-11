package com.biit.profile.persistence.repositories;

/*-
 * #%L
 * Profile Matcher (Persistence)
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

import com.biit.profile.persistence.entities.cadt.CadtArchetype;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.entities.cadt.CardSelection;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
@Test(groups = {"CadtIndividualProfileValidation"})
public class CadtIndividualProfileValidationTest {


    private void createCadtIndividualProfile(
            CadtArchetype feminineFirstSelection, CadtArchetype feminineShadowed, CadtArchetype feminineSecondSelection, CadtArchetype feminineDiscarded,
            CadtArchetype maleFirstSelection, CadtArchetype maleShadowed, CadtArchetype maleSecondSelection, CadtArchetype maleDiscarded,
            CadtCompetence... competences) {
        final CadtIndividualProfile profile = new CadtIndividualProfile();

        profile.assignCard(feminineFirstSelection, CardSelection.FIRST);
        profile.assignCard(feminineShadowed, CardSelection.SHADOWED);
        profile.assignCard(feminineSecondSelection, CardSelection.SECOND);
        profile.assignCard(feminineDiscarded, CardSelection.DISCARDED);

        profile.assignCard(maleFirstSelection, CardSelection.FIRST);
        profile.assignCard(maleShadowed, CardSelection.SHADOWED);
        profile.assignCard(maleSecondSelection, CardSelection.SECOND);
        profile.assignCard(maleDiscarded, CardSelection.DISCARDED);

        for (CadtCompetence competence : competences) {
            profile.assignCompetence(competence);
        }

        profile.validate();
    }

    @Test
    public void validProfiles() {
        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.STRATEGIST, CadtArchetype.RECEPTIVE, CadtArchetype.INNOVATOR,
                CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER, CadtArchetype.SCIENTIST,
                CadtCompetence.BUSINESS_MINDED, CadtCompetence.COMMUNICATION_SKILLS, CadtCompetence.DIRECTION, CadtCompetence.FUTURE, CadtCompetence.INITIATIVE,
                CadtCompetence.COOPERATION, CadtCompetence.LEADERSHIP, CadtCompetence.TENACITY, CadtCompetence.MULTICULTURAL_SENSITIVITY, CadtCompetence.FLEXIBILITY);
    }

    @Test(expectedExceptions = InvalidProfileValueException.class)
    public void missingArchetype() {
        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.STRATEGIST, CadtArchetype.RECEPTIVE, null,
                CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER, CadtArchetype.SCIENTIST,
                CadtCompetence.BUSINESS_MINDED, CadtCompetence.COMMUNICATION_SKILLS, CadtCompetence.DIRECTION, CadtCompetence.FUTURE, CadtCompetence.INITIATIVE,
                CadtCompetence.COOPERATION, CadtCompetence.LEADERSHIP, CadtCompetence.TENACITY, CadtCompetence.MULTICULTURAL_SENSITIVITY, CadtCompetence.FLEXIBILITY);
    }


    @Test(expectedExceptions = InvalidProfileValueException.class)
    public void duplicatedArchetype() {
        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.STRATEGIST, CadtArchetype.RECEPTIVE, CadtArchetype.VISIONARY,
                CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER, CadtArchetype.SCIENTIST,
                CadtCompetence.BUSINESS_MINDED, CadtCompetence.COMMUNICATION_SKILLS, CadtCompetence.DIRECTION, CadtCompetence.FUTURE, CadtCompetence.INITIATIVE,
                CadtCompetence.COOPERATION, CadtCompetence.LEADERSHIP, CadtCompetence.TENACITY, CadtCompetence.MULTICULTURAL_SENSITIVITY, CadtCompetence.FLEXIBILITY);
    }

    @Test(expectedExceptions = InvalidProfileValueException.class)
    public void duplicatedCompetence() {
        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.STRATEGIST, CadtArchetype.RECEPTIVE, CadtArchetype.INNOVATOR,
                CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER, CadtArchetype.SCIENTIST,
                CadtCompetence.BUSINESS_MINDED, CadtCompetence.BUSINESS_MINDED, CadtCompetence.DIRECTION, CadtCompetence.FUTURE, CadtCompetence.INITIATIVE,
                CadtCompetence.COOPERATION, CadtCompetence.LEADERSHIP, CadtCompetence.TENACITY, CadtCompetence.MULTICULTURAL_SENSITIVITY, CadtCompetence.FLEXIBILITY);
    }

    @Test(expectedExceptions = InvalidProfileValueException.class)
    public void lessCompetences() {
        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.STRATEGIST, CadtArchetype.RECEPTIVE, CadtArchetype.INNOVATOR,
                CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER, CadtArchetype.SCIENTIST,
                CadtCompetence.BUSINESS_MINDED, CadtCompetence.COMMUNICATION_SKILLS, CadtCompetence.DIRECTION, CadtCompetence.FUTURE, CadtCompetence.INITIATIVE,
                CadtCompetence.COOPERATION, CadtCompetence.LEADERSHIP, CadtCompetence.TENACITY, CadtCompetence.MULTICULTURAL_SENSITIVITY);
    }

    @Test(expectedExceptions = InvalidProfileValueException.class)
    public void moreCompetences() {
        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.STRATEGIST, CadtArchetype.RECEPTIVE, CadtArchetype.INNOVATOR,
                CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER, CadtArchetype.SCIENTIST,
                CadtCompetence.BUSINESS_MINDED, CadtCompetence.COMMUNICATION_SKILLS, CadtCompetence.DIRECTION, CadtCompetence.FUTURE, CadtCompetence.INITIATIVE,
                CadtCompetence.COOPERATION, CadtCompetence.LEADERSHIP, CadtCompetence.TENACITY, CadtCompetence.MULTICULTURAL_SENSITIVITY, CadtCompetence.FLEXIBILITY,
                CadtCompetence.PROBLEM_ANALYSIS);
    }


}
