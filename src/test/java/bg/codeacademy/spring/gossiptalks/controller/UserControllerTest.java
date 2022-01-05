package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
@ContextConfiguration(loader = SpringBootContextLoader.class)
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public class UserControllerTest extends AbstractTestNGSpringContextTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  private Long userId;
  private Long user1Id;

  @BeforeMethod
  public void setUp() {
    User user = new User();
    user.setUsername("admin");
    user.setEmail("admin@admin.bg");
    user.setName("admin");
    user.setPassword("admin1!A");
    userRepository.save(user);
    this.userId = userRepository.findByUsername("admin").orElse(new User()).getId();

    User user1 = new User();
    user1.setUsername("admin1");
    user1.setEmail("admin1@admin.bg");
    user1.setName("admin1");
    user1.setPassword("admin1!A");
    userRepository.save(user1);
    this.user1Id = userRepository.findByUsername("admin1").orElse(new User()).getId();

  }

  @AfterMethod
  public void tearDown() {
    userRepository.deleteById(userId);
    userRepository.deleteById(user1Id);
  }

  @Test
  public void registerUser_returnCreated_ifIsAnonymous() throws Exception {

    mockMvc
        .perform(multipart("/api/v1/users")
            .param("email", "admin2@admin.bg")
            .param("name", "admin2")
            .param("username", "admin2")
            .param("password", "admin1!A")
            .param("passwordConfirmation", "admin1!A")
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().string("Successful operation"));
  }

  @Test
  public void registerUser_returnPasswordDoesntMatch_ifPasswordDoesntMatch() throws Exception {

    mockMvc
        .perform(multipart("/api/v1/users").param("email", "admin3@admin.bg")
            .param("name", "admin")
            .param("username", "admin3")
            .param("password", "admin1!A")
            .param("passwordConfirmation", "admin1!")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error").value("Passwords do not match!"));
  }

  @Test
  public void registerUser_returnBadRequest_ifAreNotValidUserFields() throws Exception {

    Random random = new Random();
    String email = "admin"+ random.nextInt(50) + "@admin.bg@@@!";
    mockMvc
        .perform(multipart("/api/v1/users").param("email", email)
            .param("name", "admin#e!1")
            .param("username", "admin#e!1")
            .param("password", "admin1")
            .param("passwordConfirmation", "admin1")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error").value("Invalid fields!"))
        .andExpect(jsonPath("username").exists())
        .andExpect(jsonPath("email").exists())
        .andExpect(jsonPath("password").exists());
  }

  @Test
  public void registerUser_returnFailed_ifIsAlreadyRegistered() throws Exception {

    mockMvc
        .perform(multipart("/api/v1/users").param("email", "admin@admin.bg")
            .param("name", "admin")
            .param("username", "admin")
            .param("password", "admin1!A")
            .param("passwordConfirmation", "admin1!A")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error").value("Failed - the user already exists."));
  }



  @Test
  public void listUsers_returnUnauthorized_ifNoLoggedUser() throws Exception {
    mockMvc.perform(get("/api/v1/users")).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/api/v1/users?f=true")).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/api/v1/users?name=mil")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void listUsers_returnOk_ifUserIsLoggedIn() throws Exception {
    mockMvc.perform(get("/api/v1/users")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void listUserFollowing_returnOk_ifUserIsLoggedIn() throws Exception {
    mockMvc.perform(get("/api/v1/users?f=true"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void listUserNameSearch_returnOk_ifUserIsLoggedIn() throws Exception {
    mockMvc.perform(get("/api/v1/users?name=mil"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void listUserNameSearchFollowing_returnOk_ifUserIsLoggedIn() throws Exception {
    mockMvc.perform(get("/api/v1/users?f=true&name=mil"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void followUser_returnSuccess_ifFTrue() throws Exception {

    mockMvc
        .perform(multipart("/api/v1/users/admin1/follow").param("follow", "true")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("following").value("true"))
        .andExpect(jsonPath("username").value("admin1"));
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void followUser_returnSame_ifFTrueAndIsAlreadySubscribed() throws Exception {

    mockMvc
        .perform(multipart("/api/v1/users/admin1/follow").param("follow", "true")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("following").value("true"))
        .andExpect(jsonPath("username").value("admin1"));

    mockMvc
        .perform(multipart("/api/v1/users/admin1/follow").param("follow", "true")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("following").value("true"))
        .andExpect(jsonPath("username").value("admin1"));
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void followUser_returnError_ifFTrueAndUserIsCurrent() throws Exception {

    mockMvc
        .perform(multipart("/api/v1/users/admin/follow").param("follow", "true")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error").value("You cannot follow your account!"));
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void followUser_returnFollowingFalse_ifFFalse() throws Exception {

    mockMvc
        .perform(multipart("/api/v1/users/admin1/follow").param("follow", "false")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("following").value("false"))
        .andExpect(jsonPath("username").value("admin1"));
  }

  @Test
  @WithMockUser(username = "admin", password = "admin1!A")
  public void getCurrentUser_returnUser_ifIsAuthenticated() throws Exception {

    mockMvc
        .perform(get("/api/v1/users/me")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("admin"))
        .andExpect(jsonPath("username").value("admin"));
  }

  @Test
  public void getCurrentUser_returnError_ifIsUnauthorized() throws Exception {

    mockMvc
        .perform(get("/api/v1/users/me")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isUnauthorized());
  }

}