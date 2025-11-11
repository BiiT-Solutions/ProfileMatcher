package com.biit.profile.core;

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
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Test(groups = {"profileTest"})
@Listeners(TestListener.class)
public class ProfileTest extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";
    private static final String PROFILE_NAME = "Profile Creation 1";

    @Autowired
    private ProfileController profileController;

    private ProfileDTO savedProfileDTO;

    private String toJson(DroolsSubmittedForm droolsSubmittedForm) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(droolsSubmittedForm);
    }

    public static DroolsSubmittedForm fromJson(String json) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(json, DroolsSubmittedForm.class);
    }

    @Test
    public void generateProfile() throws FileNotFoundException, JsonProcessingException {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final ProfileDTO profileDTO = new ProfileDTO(PROFILE_NAME, toJson(droolsSubmittedForm));
        savedProfileDTO = profileController.create(profileDTO, null);
        Assert.assertNotNull(savedProfileDTO.getId());

        Assert.assertEquals(profileController.getByName(PROFILE_NAME).getContent(), toJson(droolsSubmittedForm));
    }

    @Test(dependsOnMethods = "generateProfile")
    public void assignUserToProfile() {
        profileController.assignProfiles(UUID.randomUUID(), List.of(savedProfileDTO), null);
        Assert.assertEquals(profileController.getUsers(savedProfileDTO.getId()).size(), 1);
    }

    @Test(dependsOnMethods = {"assignUserToProfile"})
    public void deleteProfile() {
        profileController.delete(savedProfileDTO, null);
        Assert.assertEquals(profileController.getUsers(savedProfileDTO.getId()).size(), 0);
    }

}
