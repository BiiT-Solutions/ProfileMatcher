package com.biit.profile.core;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.profile.core.controllers.ProfileController;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@Test(groups = {"profileTest"})
@Listeners(TestListener.class)
public class ProfileTest extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";
    private static final String PROFILE_NAME = "Profile Creation 1";

    @Autowired
    private ProfileController profileController;

    public String toJson(DroolsSubmittedForm droolsSubmittedForm) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(droolsSubmittedForm);
    }

    public static DroolsSubmittedForm fromJson(String json) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(json, DroolsSubmittedForm.class);
    }

    @Test
    public void generateProfile() throws FileNotFoundException, JsonProcessingException {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final ProfileDTO profileDTO = new ProfileDTO(PROFILE_NAME, toJson(droolsSubmittedForm));
        final ProfileDTO savedProfileDTO = profileController.create(profileDTO, null);
        Assert.assertNotNull(savedProfileDTO.getId());

        Assert.assertEquals(profileController.getByName(PROFILE_NAME).getContent(), toJson(droolsSubmittedForm));
    }

}