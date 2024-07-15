package com.biit.profile.test.api;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.core.models.ProfileDTO;
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
public class ProfileServicesTests extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";
    private static final String PROFILE_NAME = "Profile Creation 1";
    private static final String PROFILE_TYPE = "Job Offer";
    private static final String PROFILE_TRACKING_CODE = UUID.randomUUID().toString();

    private static final String USER_USERNAME_1 = "username1";
    private static final String USER_NAME_1 = "User1";
    private static final String USER_LASTNAME_1 = "Lastname1";
    private static final String USER_EMAIL_1 = "email1@test.com";

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

    private MockMvc mockMvc;

    private String adminJwtToken;

    private ProfileDTO profile;


    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
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
    public void createProfile() throws Exception {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final ProfileDTO profileDTO = new ProfileDTO(PROFILE_NAME, toJson(droolsSubmittedForm));
        profileDTO.setType(PROFILE_TYPE);
        profileDTO.setTrackingCode(PROFILE_TRACKING_CODE);

        final MvcResult createResult = this.mockMvc
                .perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(profileDTO))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        profile =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO.class);
        Assert.assertEquals(profile.getName(), PROFILE_NAME);
        Assert.assertEquals(profile.getContent(), toJson(droolsSubmittedForm));
    }


    @Test(dependsOnMethods = "createProfile")
    public void searchProfileByName() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles/names/" + PROFILE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final ProfileDTO profile =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO.class);
        Assert.assertEquals(profile.getName(), PROFILE_NAME);
    }


    @Test(dependsOnMethods = "createProfile")
    public void searchProfileByType() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles/types/" + PROFILE_TYPE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<ProfileDTO> profiles =
                Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO[].class));
        Assert.assertEquals(profiles.size(), 1);
        Assert.assertEquals(profiles.get(0).getName(), PROFILE_NAME);
        Assert.assertEquals(profiles.get(0).getType(), PROFILE_TYPE);
    }


    @Test(dependsOnMethods = "createProfile")
    public void searchProfileByTrackingCode() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles/tracking-codes/" + PROFILE_TRACKING_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<ProfileDTO> profiles =
                Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileDTO[].class));
        Assert.assertEquals(profiles.size(), 1);
        Assert.assertEquals(profiles.get(0).getName(), PROFILE_NAME);
        Assert.assertEquals(profiles.get(0).getTrackingCode(), PROFILE_TRACKING_CODE);
    }


    @Test(dependsOnMethods = "createProfile")
    public void addCandidateToProfile() throws Exception {
        final List<UserDTO> users = new ArrayList<>();
        UserDTO userDTO1 = new UserDTO(USER_USERNAME_1, USER_NAME_1, USER_LASTNAME_1, USER_EMAIL_1);
        userDTO1.setId(1L);
        users.add(userDTO1);

        UserDTO userDTO2 = new UserDTO(USER_USERNAME_2, USER_NAME_2, USER_LASTNAME_2, USER_EMAIL_2);
        userDTO2.setId(2L);
        users.add(userDTO2);

        final MvcResult createResult = this.mockMvc
                .perform(post("/profiles/" + profile.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(users))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test(dependsOnMethods = "addCandidateToProfile")
    public void removeCandidateToProfile() throws Exception {
        final List<UserDTO> users = new ArrayList<>();
        UserDTO userDTO1 = new UserDTO(USER_USERNAME_1, USER_NAME_1, USER_LASTNAME_1, USER_EMAIL_1);
        userDTO1.setId(1L);
        users.add(userDTO1);

        UserDTO userDTO2 = new UserDTO(USER_USERNAME_2, USER_NAME_2, USER_LASTNAME_2, USER_EMAIL_2);
        userDTO2.setId(2L);
        users.add(userDTO2);

        final MvcResult createResult = this.mockMvc
                .perform(post("/profiles/" + profile.getId() + "/users/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(users))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
