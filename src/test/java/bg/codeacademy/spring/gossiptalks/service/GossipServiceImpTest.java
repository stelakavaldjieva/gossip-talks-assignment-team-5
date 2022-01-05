package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;

@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public class GossipServiceImpTest extends AbstractTestNGSpringContextTests
{


  @Mock
  public GossipRepository gossipRepository;
  @Mock
  UserService     userService;

  private GossipServiceImp gossipService;

  @BeforeMethod
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);
    gossipService = new GossipServiceImp(gossipRepository, userService);
  }

  @Test
  void getByUser_Success()
  {

    final Pageable pageable = PageRequest.of(0, 10);

    User user = new User(1L, "username", "name", "password");
    user.setFriends(new HashSet<>());
    Mockito.doReturn(user).when(userService).getCurrentUser();
    gossipService.getByUser("username", pageable);

    Mockito.verify(gossipRepository, Mockito.times(1))
        .findByUserUsernameOrderByDatetimeDesc("username", pageable);

  }

  @Test
  void getAllSubscriberGossips_Success()
  {

    final Pageable pageable = PageRequest.of(0, 10);

    User user = new User(1L, "username", "name", "password");

    Mockito.doReturn(user).when(userService).getCurrentUser();

    gossipService.getAllSubscriberGossips(pageable);

    Mockito.verify(gossipRepository, Mockito.atLeast(0))
        .findFollowingUsers(1L, pageable);

  }

  @Test
  void createGossip_Success()
  {
    gossipService.createGossip("text");
  }
}