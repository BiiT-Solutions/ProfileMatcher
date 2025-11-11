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
import com.biit.profile.core.controllers.ProjectController;
import com.biit.profile.core.exceptions.ProfileNotAssignedToProjectException;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.core.models.ProjectDTO;
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
@Test(groups = {"projectTest"})
@Listeners(TestListener.class)
public class ProjectTest extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";

    private static final String PROFILE_NAME_1 = "Profile1";
    private static final String PROFILE_NAME_2 = "Profile2";
    private static final String PROJECT_NAME_1 = "Project1";
    private static final String PROJECT_NAME_2 = "Project2";

    @Autowired
    private ProfileController profileController;

    @Autowired
    private ProjectController projectController;

    private ProfileDTO savedProfile1DTO;
    private ProfileDTO savedProfile2DTO;
    private ProjectDTO savedProject1DTO;
    private ProjectDTO savedProject2DTO;

    private String toJson(DroolsSubmittedForm droolsSubmittedForm) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(droolsSubmittedForm);
    }

    @Test
    public void generateProfile() throws FileNotFoundException, JsonProcessingException {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));

        ProfileDTO profileDTO = new ProfileDTO(PROFILE_NAME_1, toJson(droolsSubmittedForm));
        savedProfile1DTO = profileController.create(profileDTO, null);
        Assert.assertNotNull(savedProfile1DTO.getId());
        Assert.assertNotNull(profileController.getByName(PROFILE_NAME_1));

        profileDTO = new ProfileDTO(PROFILE_NAME_2, toJson(droolsSubmittedForm));
        savedProfile2DTO = profileController.create(profileDTO, null);
        Assert.assertNotNull(savedProfile1DTO.getId());
        Assert.assertNotNull(profileController.getByName(PROFILE_NAME_2));
    }


    @Test(dependsOnMethods = "generateProfile")
    public void generateProject() {
        ProjectDTO projectDTO = new ProjectDTO(PROJECT_NAME_1);
        savedProject1DTO = projectController.create(projectDTO, null);
        Assert.assertNotNull(savedProject1DTO.getId());

        projectDTO = new ProjectDTO(PROJECT_NAME_2);
        savedProject2DTO = projectController.create(projectDTO, null);
        Assert.assertNotNull(savedProject2DTO.getId());
    }

    @Test(dependsOnMethods = {"generateProject", "generateProfile"}, expectedExceptions = ProfileNotAssignedToProjectException.class)
    public void assignUsersToProfileNotAllowed() {
        projectController.assignUsersToProfiles(savedProject1DTO.getId(), UUID.randomUUID(), List.of(savedProfile1DTO), null);
    }

    @Test(dependsOnMethods = {"assignUsersToProfileNotAllowed"})
    public void assignProfileToProject() {
        projectController.assignByProject(savedProject1DTO.getId(), List.of(savedProfile1DTO), null);
    }

    @Test(dependsOnMethods = {"assignProfileToProject"})
    public void assignUsersToProfileAssigned() {
        projectController.assignUsersToProfiles(savedProject1DTO.getId(), UUID.randomUUID(), List.of(savedProfile1DTO), null);
        Assert.assertEquals(projectController.getUsers(savedProject1DTO.getId(), savedProfile1DTO.getId()).size(), 1);
        Assert.assertEquals(profileController.getByProjectId(savedProject1DTO.getId()).size(), 1);
        Assert.assertEquals(projectController.getUsers(savedProject2DTO.getId(), savedProfile1DTO.getId()).size(), 0);
        Assert.assertEquals(projectController.getUsers(savedProject1DTO.getId(), savedProfile2DTO.getId()).size(), 0);
    }

    @Test(dependsOnMethods = {"assignProfileToProject"})
    public void deleteProjectDeletesAlsoRelations() {
        projectController.delete(savedProject1DTO, null);
        Assert.assertEquals(projectController.getUsers(savedProject1DTO.getId(), savedProfile1DTO.getId()).size(), 0);
        Assert.assertEquals(profileController.getByProjectId(savedProject1DTO.getId()).size(), 0);
    }
}
