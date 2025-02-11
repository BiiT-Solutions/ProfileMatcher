package com.biit.profile.persistence.repositories;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
@Test(groups = {"ProfileRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProfileRepositoryTests extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH_1 = "drools/droolsProfile1.json";
    private static final String DROOLS_FORM_FILE_PATH_2 = "drools/droolsProfile2.json";
    private static final String DROOLS_FORM_FILE_PATH_3 = "drools/droolsProfile3.json";

    @Autowired
    private ProfileRepository repository;

    private void createProfile(String profileName, DroolsSubmittedForm form) {
        final Profile profile = new Profile();
        profile.setName(profileName);
        profile.setEntity(form);
        profile.populateFields();

        repository.save(profile);
    }

    @BeforeClass
    public void initIndividualProfiles() throws FileNotFoundException, JsonProcessingException {
        createProfile(DROOLS_FORM_FILE_PATH_1, DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH_1, StandardCharsets.UTF_8)));
        createProfile(DROOLS_FORM_FILE_PATH_2, DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH_2, StandardCharsets.UTF_8)));
        createProfile(DROOLS_FORM_FILE_PATH_3, DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH_3, StandardCharsets.UTF_8)));

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
