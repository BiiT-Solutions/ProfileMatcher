package com.biit.profile.test.api;

import com.biit.profile.core.models.CadtIndividualProfileDTO;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.profile.persistence.entities.cadt.CadtArchetype;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.profile.persistence.entities.cadt.CardSelection;
import com.biit.profile.persistence.repositories.CadtIndividualProfileRepository;
import com.biit.server.security.model.AuthRequest;
import com.biit.usermanager.client.providers.AuthenticatedUserProvider;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Test(groups = {"cadtIndividualProfileServices"})
public class CadtIndividualProfilesServicesTests extends AbstractTestNGSpringContextTests {

    private final static String USER_NAME = "user";
    private final static String USER_PASSWORD = "password";

    private final static String JWT_SALT = "4567";

    @Autowired
    private CadtIndividualProfileRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    private MockMvc mockMvc;

    private String adminJwtToken;

    private ProfileDTO profile;


    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


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
                .perform(get("/individual-reports/competences/" + 10)
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

        final CadtIndividualProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), CadtIndividualProfileDTO[].class);
        Assert.assertEquals(profiles.length, 1);
    }


    @Test(dependsOnMethods = "setAdminAuthentication")
    public void findBy9From9Competences() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/individual-reports/competences/" + 9)
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

        final CadtIndividualProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), CadtIndividualProfileDTO[].class);
        Assert.assertEquals(profiles.length, 1);
    }

    @Test(dependsOnMethods = "setAdminAuthentication")
    public void notFindBy10From9Competences() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/individual-reports/competences/" + 10)
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

        final CadtIndividualProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), CadtIndividualProfileDTO[].class);
        Assert.assertEquals(profiles.length, 0);
    }

    @Test(dependsOnMethods = "setAdminAuthentication")
    public void findBy3From4Competences() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/individual-reports/competences/" + 3)
                        .param("competence", CadtCompetence.INITIATIVE.getTag())
                        .param("competence", CadtCompetence.FLEXIBILITY.getTag())
                        .param("competence", CadtCompetence.GOAL_SETTING.getTag())
                        .param("competence", CadtCompetence.COOPERATION.getTag())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final CadtIndividualProfileDTO[] profiles =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), CadtIndividualProfileDTO[].class);
        Assert.assertEquals(profiles.length, 3);
    }


}
