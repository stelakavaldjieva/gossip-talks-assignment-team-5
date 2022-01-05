package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import bg.codeacademy.spring.gossiptalks.service.UserService;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
@ContextConfiguration(loader = SpringBootContextLoader.class)
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public class GossipControllerTest extends AbstractTestNGSpringContextTests
{

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;

  @Autowired
  GossipRepository gossipRepository;

  private Long userId;
  private Long user1Id;

  @BeforeMethod
  public void setUp()
  {
    User user = new User();
    user.setUsername("admin");
    user.setEmail("admin@admin.bg");
    user.setName("admin");
    user.setPassword("admin1!A");
    userService.registerAccount(user.getEmail(),user.getUsername(),user.getName(),user.getPassword(),user.getPassword());
    this.userId = userService.getByUsername("admin").getId();

    User user1 = new User();
    user1.setUsername("admin1");
    user1.setEmail("admin1@admin.bg");
    user1.setName("admin1");
    user1.setPassword("admin1!A");
    userService.registerAccount(user1.getEmail(),user1.getUsername(),user1.getName(),user1.getPassword(),user1.getPassword());
    this.user1Id = userService.getByUsername("admin1").getId();

    Gossip gossip = new Gossip();
    gossip.setText("admin 1 text");
    gossip.setDatetime(LocalDateTime.now());
    gossip.setUser(userService.getByUsername("admin1"));
    gossipRepository.save(gossip);

    followUser();




  }

  @AfterMethod
  public void tearDown()
  {
    userRepository.deleteById(userId);
    userRepository.deleteById(user1Id);
  }

  private void followUser() {
    if (userService.getCurrentUser().getUsername().equals("admin"))
    userService.userFollow("admin1",true);
  }

  @Test @WithMockUser(username = "admin",password = "admin1!A")
  public void createGossip_returnCreatedWithHtmlEscaped_IfGossipIsValid() throws Exception
  {
    mockMvc
        .perform(multipart("/api/v1/gossips").param("text", "<b>This must be normal text</b>")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("text").value("You wrote forbidden symbols!"));

  }

  @Test @WithMockUser(username = "admin",password = "admin1!A")
  public void createGossip_returnError_IfGossipIsBlank() throws Exception
  {
    mockMvc
        .perform(multipart("/api/v1/gossips").param("text", "       ")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("text").value("You haven't written anything!"));

  }

  @Test @WithMockUser(username = "admin",password = "admin1!A")
  public void getUserGossips_returnGossips_IfValidUsername() throws Exception
  {
    mockMvc
        .perform(multipart("/api/v1/gossips").param("text", "my first gossip")
        ).andDo(MockMvcResultHandlers.print());
    mockMvc
        .perform(multipart("/api/v1/gossips").param("text", "my second gossip")
  ).andDo(MockMvcResultHandlers.print());
    mockMvc
        .perform(multipart("/api/v1/gossips").param("text", "my third gossip")
  ).andDo(MockMvcResultHandlers.print());

    mockMvc.perform(get("/api/v1/users/admin/gossips"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("total").value("3"));
  }

  @Test @WithMockUser(username = "admin",password = "admin1!A")
  public void getUserGossips_returnErrorNotFound_IfInvalidUsername() throws Exception
  {

    mockMvc.perform(get("/api/v1/users/invalid/gossips"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("error").value("User not found!"));
  }

  @Test @WithMockUser(username = "admin",password = "admin1!A")
public void readAllGossips_returnContent_IfValid() throws Exception
{

  mockMvc.perform(get("/api/v1/gossips"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(jsonPath("content").isNotEmpty());
}

  @Test @WithMockUser(username = "admin1",password = "admin1!A")
  public void readAllGossips_returnEmpty_IfFollowNoOne() throws Exception
  {

    mockMvc.perform(get("/api/v1/gossips"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("content").isEmpty());
  }
}