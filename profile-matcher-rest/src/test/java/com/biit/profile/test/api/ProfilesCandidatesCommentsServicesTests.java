package com.biit.profile.test.api;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.core.models.ProfileCandidateCommentDTO;
import com.biit.profile.core.models.ProfileDTO;
import com.biit.server.security.model.AuthRequest;
import com.biit.usermanager.client.providers.AuthenticatedUserProvider;
import com.biit.usermanager.dto.UserDTO;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Test(groups = {"profileCandidateCommentServices"})
public class ProfilesCandidatesCommentsServicesTests extends AbstractTestNGSpringContextTests {

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";
    private static final String PROFILE_NAME = "Profile Creation 1";
    private static final String PROFILE_TYPE = "Job Offer";
    private static final String PROFILE_TRACKING_CODE = UUID.randomUUID().toString();

    private static final UUID USER_ID_1 = UUID.randomUUID();
    private static final String USER_USERNAME_1 = "username1";
    private static final String USER_NAME_1 = "User1";
    private static final String USER_LASTNAME_1 = "Lastname1";
    private static final String USER_EMAIL_1 = "email1@test.com";

    private static final UUID USER_ID_2 = UUID.randomUUID();
    private static final String USER_USERNAME_2 = "username2";
    private static final String USER_NAME_2 = "User2";
    private static final String USER_LASTNAME_2 = "Lastname2";
    private static final String USER_EMAIL_2 = "email2@test.com";

    private static final String USER_COMMENT_1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla facilisis arcu nisi, sed molestie velit egestas quis. Morbi nec congue nibh, quis fermentum ante. Nam dignissim nisl a leo dignissim laoreet. Phasellus non ultricies nunc. Sed tempor, nunc in rutrum fringilla, nisi felis convallis ipsum, ut molestie libero felis quis nisl. Morbi feugiat dui id maximus dictum. Nunc ac consectetur metus, in bibendum purus.";
    private static final String USER_COMMENT_2 = "Donec congue, quam ac dignissim condimentum, ex nisl pulvinar magna, at convallis ipsum nunc in dolor. Mauris sed urna vel justo condimentum fermentum nec in ex. In sed pharetra turpis. Fusce mattis convallis justo, ac hendrerit velit mollis quis. Donec quis sapien porta, pretium purus et, sodales purus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Integer ac urna fermentum, maximus sem ac, facilisis metus. Maecenas fringilla nec leo ut semper. Proin sed ante massa. Proin efficitur laoreet lacus eget consequat. Donec arcu sapien, euismod vitae iaculis ac, tincidunt et diam. Sed sed massa eros. Vivamus lobortis ut nisi at facilisis. Ut eu est mattis, bibendum ligula mattis, suscipit nulla.";

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
    public void commentCanNotyBeAddedToCandidate1ToProfile() throws Exception {
        this.mockMvc
                .perform(put("/profiles-candidates-comments/profiles/" + profile.getId() + "/users/" + USER_ID_1 + "/comments/" + USER_COMMENT_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }


    @Test(dependsOnMethods = "commentCanNotyBeAddedToCandidate1ToProfile")
    public void addCandidateToProfile() throws Exception {
        final List<UserDTO> users = new ArrayList<>();
        UserDTO userDTO1 = new UserDTO(USER_USERNAME_1, USER_NAME_1, USER_LASTNAME_1, USER_EMAIL_1);
        userDTO1.setUID(USER_ID_1.toString());
        users.add(userDTO1);

        UserDTO userDTO2 = new UserDTO(USER_USERNAME_2, USER_NAME_2, USER_LASTNAME_2, USER_EMAIL_2);
        userDTO2.setUID(USER_ID_2.toString());
        users.add(userDTO2);

        this.mockMvc
                .perform(post("/profiles/" + profile.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(users))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test(dependsOnMethods = "addCandidateToProfile")
    public void addCommentTooLongToCandidate1ToProfile() throws Exception {
        this.mockMvc
                .perform(post("/profiles-candidates-comments/profiles/" + profile.getId() + "/users/" + USER_ID_1 + "/comments")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(StringUtils.repeat(USER_COMMENT_1, 30))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }


    @Test(dependsOnMethods = "addCommentTooLongToCandidate1ToProfile")
    public void addCommentToCandidate1ToProfile() throws Exception {
        this.mockMvc
                .perform(post("/profiles-candidates-comments/profiles/" + profile.getId() + "/users/" + USER_ID_1 + "/comments")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(USER_COMMENT_1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test(dependsOnMethods = "addCandidateToProfile")
    public void addCommentToCandidate2ToProfile() throws Exception {
        this.mockMvc
                .perform(post("/profiles-candidates-comments/profiles/" + profile.getId() + "/users/" + USER_ID_2 + "/comments")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(USER_COMMENT_2)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test(dependsOnMethods = "addCommentToCandidate1ToProfile")
    public void getCommentFromCandidate1OnProfile() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles-candidates-comments/profiles/" + profile.getId() + "/users/" + USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final ProfileCandidateCommentDTO profileCandidateCommentDTO =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileCandidateCommentDTO.class);
        Assert.assertEquals(profileCandidateCommentDTO.getComment(), USER_COMMENT_1);
    }


    @Test(dependsOnMethods = "addCommentToCandidate2ToProfile")
    public void getCommentFromCandidate2OnProfile() throws Exception {
        final MvcResult createResult = this.mockMvc
                .perform(get("/profiles-candidates-comments/profiles/" + profile.getId() + "/users/" + USER_ID_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final ProfileCandidateCommentDTO profileCandidateCommentDTO =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), ProfileCandidateCommentDTO.class);
        Assert.assertEquals(profileCandidateCommentDTO.getComment(), USER_COMMENT_2);
    }

}
