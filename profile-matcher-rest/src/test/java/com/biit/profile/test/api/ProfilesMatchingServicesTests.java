package com.biit.profile.test.api;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.server.security.model.AuthRequest;
import com.biit.usermanager.client.providers.AuthenticatedUserProvider;
import com.biit.usermanager.dto.UserDTO;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Test(groups = {"profileServices"})
public class ProfilesMatchingServicesTests extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH_1 = "drools/droolsProfile1.json";
    private static final String DROOLS_FORM_FILE_PATH_2 = "drools/droolsProfile2.json";
    private static final String DROOLS_FORM_FILE_PATH_3 = "drools/droolsProfile3.json";

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";
    private static final String PROFILE_NAME = "Profile Creation 1";
    private static final String PROFILE_TYPE = "Job Offer";
    private static final String PROFILE_TRACKING_CODE = UUID.randomUUID().toString();

    private static final Long USER_ID_1 = 1L;
    private static final String USER_USERNAME_1 = "username1";
    private static final String USER_NAME_1 = "User1";
    private static final String USER_LASTNAME_1 = "Lastname1";
    private static final String USER_EMAIL_1 = "email1@test.com";

    private static final Long USER_ID_2 = 2L;
    private static final String USER_USERNAME_2 = "username2";
    private static final String USER_NAME_2 = "User2";
    private static final String USER_LASTNAME_2 = "Lastname2";
    private static final String USER_EMAIL_2 = "email2@test.com";

    private final static String USER_NAME = "user";
    private final static String USER_PASSWORD = "password";

    private final static String JWT_SALT = "4567";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProfileRepository repository;

    private MockMvc mockMvc;

    private String adminJwtToken;

    private ProfileDTO profile;


    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


    private void createProfile(String profileName, DroolsSubmittedForm form) {
        final Profile profile = new Profile();
        profile.setName(profileName);
        profile.setEntity(form);
        profile.populateFields();

        repository.save(profile);
    }


    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }


    @BeforeClass
    public void addUser() {
        //Create the admin user
        authenticatedUserProvider.createUser(USER_NAME, USER_NAME, USER_PASSWORD);
    }

    @BeforeClass
    public void initIndividualProfiles() throws FileNotFoundException, JsonProcessingException {
        createProfile(DROOLS_FORM_FILE_PATH_1, DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH_1, StandardCharsets.UTF_8)));
        createProfile(DROOLS_FORM_FILE_PATH_2, DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH_2, StandardCharsets.UTF_8)));
        createProfile(DROOLS_FORM_FILE_PATH_3, DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH_3, StandardCharsets.UTF_8)));

        Assert.assertEquals(repository.count(), 3);
    }

    @Test
    public void checkAuthentication() {
        //Check the admin user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER_NAME, JWT_SALT + USER_PASSWORD));
    }


    @Test
    public void setAdminAuthentication() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername(USER_NAME);
        request.setPassword(USER_PASSWORD);

        final MvcResult createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        adminJwtToken = createResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        Assert.assertNotNull(adminJwtToken);
    }

    @Test(dependsOnMethods = "setAdminAuthentication")
    public void findBy10From10Competences() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles/competences/" + 10)
                        .param("competence", CadtCompetence.BUSINESS_MINDED.getTag())
                        .param("competence", CadtCompetence.COMMUNICATION_SKILLS.getTag())
                        .param("competence", CadtCompetence.DIRECTION.getTag())
                        .param("competence", CadtCompetence.FUTURE.getTag())
                        .param("competence", CadtCompetence.INITIATIVE.getTag())
                        .param("competence", CadtCompetence.COOPERATION.getTag())
                        .param("competence", CadtCompetence.LEADERSHIP.getTag())
                        .param("competence", CadtCompetence.TENACITY.getTag())
                        .param("competence", CadtCompetence.MULTICULTURAL_SENSITIVITY.getTag())
                        .param("competence", CadtCompetence.FLEXIBILITY.getTag())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final ProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO[].class);
        Assert.assertEquals(profiles.length, 1);
    }

    @Test(dependsOnMethods = "setAdminAuthentication")
    public void findBy9From9Competences() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles/competences/" + 9)
                        .param("competence", CadtCompetence.BUSINESS_MINDED.getTag())
                        .param("competence", CadtCompetence.COMMUNICATION_SKILLS.getTag())
                        .param("competence", CadtCompetence.DIRECTION.getTag())
                        .param("competence", CadtCompetence.FUTURE.getTag())
                        .param("competence", CadtCompetence.INITIATIVE.getTag())
                        .param("competence", CadtCompetence.COOPERATION.getTag())
                        .param("competence", CadtCompetence.LEADERSHIP.getTag())
                        .param("competence", CadtCompetence.TENACITY.getTag())
                        .param("competence", CadtCompetence.MULTICULTURAL_SENSITIVITY.getTag())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final ProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO[].class);
        Assert.assertEquals(profiles.length, 1);
    }

    @Test(dependsOnMethods = "setAdminAuthentication")
    public void notFindBy10From9Competences() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles/competences/" + 10)
                        .param("competence", CadtCompetence.BUSINESS_MINDED.getTag())
                        .param("competence", CadtCompetence.COMMUNICATION_SKILLS.getTag())
                        .param("competence", CadtCompetence.DIRECTION.getTag())
                        .param("competence", CadtCompetence.FUTURE.getTag())
                        .param("competence", CadtCompetence.INITIATIVE.getTag())
                        .param("competence", CadtCompetence.COOPERATION.getTag())
                        .param("competence", CadtCompetence.LEADERSHIP.getTag())
                        .param("competence", CadtCompetence.TENACITY.getTag())
                        .param("competence", CadtCompetence.MULTICULTURAL_SENSITIVITY.getTag())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final ProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO[].class);
        Assert.assertEquals(profiles.length, 0);
    }

    @Test(dependsOnMethods = "setAdminAuthentication")
    public void findBy3From4Competences() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles/competences/" + 3)
                        .param("competence", CadtCompetence.INITIATIVE.getTag())
                        .param("competence", CadtCompetence.FLEXIBILITY.getTag())
                        .param("competence", CadtCompetence.GOAL_SETTING.getTag())
                        .param("competence", CadtCompetence.COOPERATION.getTag())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final ProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO[].class);
        Assert.assertEquals(profiles.length, 3);
    }
}
