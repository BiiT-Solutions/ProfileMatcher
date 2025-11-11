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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

@SpringBootTest
@Test(groups = {"CadtIndividualProfileRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CadtIndividualProfileRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CadtIndividualProfileRepository repository;

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

        repository.save(profile);
    }

    @BeforeClass
    public void initIndividualProfiles() {
        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.STRATEGIST, CadtArchetype.RECEPTIVE, CadtArchetype.INNOVATOR,
                CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER, CadtArchetype.SCIENTIST,
                CadtCompetence.BUSINESS_MINDED, CadtCompetence.COMMUNICATION_SKILLS, CadtCompetence.DIRECTION, CadtCompetence.FUTURE, CadtCompetence.INITIATIVE,
                CadtCompetence.COOPERATION, CadtCompetence.LEADERSHIP, CadtCompetence.TENACITY, CadtCompetence.MULTICULTURAL_SENSITIVITY, CadtCompetence.FLEXIBILITY);


        createCadtIndividualProfile(CadtArchetype.RECEPTIVE, CadtArchetype.STRATEGIST, CadtArchetype.VISIONARY, CadtArchetype.INNOVATOR,
                CadtArchetype.SCIENTIST, CadtArchetype.TRADESMAN, CadtArchetype.LEADER, CadtArchetype.BANKER,
                CadtCompetence.INITIATIVE, CadtCompetence.DISCIPLINE, CadtCompetence.GOAL_SETTING, CadtCompetence.PLANIFICATION, CadtCompetence.PROBLEM_ANALYSIS,
                CadtCompetence.COOPERATION, CadtCompetence.JUDGEMENT, CadtCompetence.FLEXIBILITY, CadtCompetence.TENACITY, CadtCompetence.RELATIONSHIPS);

        createCadtIndividualProfile(CadtArchetype.VISIONARY, CadtArchetype.INNOVATOR, CadtArchetype.RECEPTIVE, CadtArchetype.STRATEGIST,
                CadtArchetype.TRADESMAN, CadtArchetype.SCIENTIST, CadtArchetype.LEADER, CadtArchetype.BANKER,
                CadtCompetence.INITIATIVE, CadtCompetence.DISCIPLINE, CadtCompetence.GOAL_SETTING, CadtCompetence.MULTICULTURAL_SENSITIVITY, CadtCompetence.PROBLEM_ANALYSIS,
                CadtCompetence.INTERPERSONAL_SENSITIVITY, CadtCompetence.COMMUNICATION_SKILLS, CadtCompetence.FLEXIBILITY, CadtCompetence.TENACITY, CadtCompetence.RELATIONSHIPS);

        Assert.assertEquals(repository.count(), 3);
    }


    @Test
    public void findBy10From10Competences() {
        Assert.assertEquals(repository.findByCompetencesIn(List.of(
                CadtCompetence.BUSINESS_MINDED.getTag(), CadtCompetence.COMMUNICATION_SKILLS.getTag(), CadtCompetence.DIRECTION.getTag(), CadtCompetence.FUTURE.getTag(), CadtCompetence.INITIATIVE.getTag(),
                CadtCompetence.COOPERATION.getTag(), CadtCompetence.LEADERSHIP.getTag(), CadtCompetence.TENACITY.getTag(), CadtCompetence.MULTICULTURAL_SENSITIVITY.getTag(), CadtCompetence.FLEXIBILITY.getTag()
        ), 10).size(), 1);
    }


    @Test
    public void findBy9From9Competences() {
        Assert.assertEquals(repository.findByCompetencesIn(List.of(
                CadtCompetence.BUSINESS_MINDED.getTag(), CadtCompetence.COMMUNICATION_SKILLS.getTag(), CadtCompetence.DIRECTION.getTag(), CadtCompetence.FUTURE.getTag(), CadtCompetence.INITIATIVE.getTag(),
                CadtCompetence.COOPERATION.getTag(), CadtCompetence.LEADERSHIP.getTag(), CadtCompetence.TENACITY.getTag(), CadtCompetence.MULTICULTURAL_SENSITIVITY.getTag()
        ), 9).size(), 1);
    }

    @Test
    public void notFindBy10From9Competences() {
        Assert.assertEquals(repository.findByCompetencesIn(List.of(
                CadtCompetence.BUSINESS_MINDED.getTag(), CadtCompetence.COMMUNICATION_SKILLS.getTag(), CadtCompetence.DIRECTION.getTag(), CadtCompetence.FUTURE.getTag(), CadtCompetence.INITIATIVE.getTag(),
                CadtCompetence.COOPERATION.getTag(), CadtCompetence.LEADERSHIP.getTag(), CadtCompetence.TENACITY.getTag(), CadtCompetence.MULTICULTURAL_SENSITIVITY.getTag()
        ), 10).size(), 0);
    }

    @Test
    public void findBy3From4Competences() {
        //2 competences all 3, 1 only 2, the other only 2.
        Assert.assertEquals(repository.findByCompetencesIn(List.of(
                CadtCompetence.INITIATIVE.getTag(), CadtCompetence.FLEXIBILITY.getTag(), CadtCompetence.GOAL_SETTING.getTag(), CadtCompetence.COOPERATION.getTag()
        ), 3).size(), 3);
    }
}
