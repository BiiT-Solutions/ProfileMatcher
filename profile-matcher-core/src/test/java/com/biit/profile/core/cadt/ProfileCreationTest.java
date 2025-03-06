package com.biit.profile.core.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.core.TestListener;
import com.biit.profile.core.controllers.ProfileController;
import com.biit.profile.core.providers.ProfileProvider;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@Test(groups = "profileCadt")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Listeners(TestListener.class)
public class ProfileCreationTest extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH = "drools/CADT_Profile_Creator.json";

    @Autowired
    private ProfileController profileController;

    @Autowired
    private ProfileProvider profileProvider;


    @Test
    public void processDroolsForm() throws FileNotFoundException, JsonProcessingException {

        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader
                .getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));

        profileController.newProfileReceived(droolsSubmittedForm, null);
        Assert.assertEquals(profileProvider.findAll().size(), 1);
        final Profile profile = profileProvider.findAll().get(0);
        Assert.assertEquals(profile.getName(), ("Gardener"));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.INTERPERSONAL_SENSITIVITY));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.COOPERATION));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.FUTURE));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.PERSUASIVENESS));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.GOAL_SETTING));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.CONSCIENTIOUSNESS));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.FLEXIBILITY));
        Assert.assertTrue(profile.getDesiredCompetences().contains(CadtCompetence.DIRECTION));
    }


}
