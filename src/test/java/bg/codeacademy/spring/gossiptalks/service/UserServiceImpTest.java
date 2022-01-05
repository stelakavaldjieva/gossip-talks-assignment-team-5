package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.exception.PasswordsDoNotMatchException;
import bg.codeacademy.spring.gossiptalks.exception.UserAlreadyExistsException;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public class UserServiceImpTest extends AbstractTestNGSpringContextTests
{

  @Mock
  public UserRepository userRepository;
  @Mock
  SecurityContext securityContext;
  @Mock
  Authentication  authentication;
  @Mock
  PasswordEncoder passwordEncoder;

  private UserServiceImp userService;

  @BeforeMethod
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);
    userService = new UserServiceImp(userRepository, null);
    //Set up current user
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    Mockito.when(userRepository.findByUsername(authentication.getName())).thenReturn(Optional.of(new User().setUsername("milka").setId(1L)));
    SecurityContextHolder.setContext(securityContext);

  }

  @Test
  public void listUsers_ReturnAll_IfNoParams()
  {

    final Pageable pageable = PageRequest.of(0, 10);

    userService.listUsers(null, Boolean.FALSE, pageable);

    Mockito.verify(userRepository, Mockito.atLeast(1))
        .findAllByOrderByGossipsCountDesc(pageable);

  }

  @Test
  public void listUsers_ReturnFollowing_IfFisTrue()
  {

    userService.listUsers(null, Boolean.TRUE, PageRequest.of(0, 10));

    Mockito.verify(userRepository, Mockito.atLeast(1))
        .findCurrentUserFriends(1L, PageRequest.of(0, 10));

  }

  @Test
  public void listUsers_ReturnFollowing_IfFisTrueAndNameNotNull()
  {

    userService.listUsers("mil", Boolean.TRUE, PageRequest.of(0, 10));

    Mockito.verify(userRepository, Mockito.atLeast(1))
        .findCurrentUserFriendsSearchingName(1L, "mil", PageRequest.of(0, 10));

  }

  @Test
  public void listUsers_ReturnAll_IfNameNotNull()
  {

    userService.listUsers("mil", Boolean.FALSE, PageRequest.of(0, 10));

    Mockito.verify(userRepository, Mockito.atLeast(1))
        .findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase("mil", "mil", PageRequest.of(0, 10));

  }

  @Test(expectedExceptions = UserAlreadyExistsException.class)
  public void registerAccount_Failure_EmailExists()
  {

    ReflectionTestUtils.setField(userService, // inject into this object
        "passwordEncoder", // assign to this field
        passwordEncoder); // object to be injected

    Optional<User> user = Optional.of(new User());

    Mockito.when(userRepository.findByEmail("email@startit.bg")).thenReturn(user);

    Mockito.when(userService.registerAccount("email@startit.bg", "username", "name",
            "Password!.1", "Password!.1"))
        .thenThrow(UserAlreadyExistsException.class);

  }

  @Test(expectedExceptions = PasswordsDoNotMatchException.class)
  public void registerAccount_Failure_PasswordsDoNotMatch()
  {

    Mockito.when(userService.registerAccount("email@startit.bg", "username", "name",
            "Password!.1", "Password!.2"))
        .thenThrow(PasswordsDoNotMatchException.class);

  }

  @Test
  public void registerAccount_Success()
  {

    ReflectionTestUtils.setField(userService, // inject into this object
        "passwordEncoder", // assign to this field
        passwordEncoder); // object to be injected

    Mockito.when(userService.registerAccount("email@startit.bg", "username", "name",
            "Password!.1", "Password!.1"))
        .thenReturn(Mockito.mock(User.class));

  }

  @Test(expectedExceptions = PasswordsDoNotMatchException.class)
  public void changePassword_Failure_PasswordsAreNull()
  {

    ReflectionTestUtils.setField(userService, // inject into this object
        "passwordEncoder", // assign to this field
        passwordEncoder); // object to be injected

    Mockito.when(userService.changePassword(null, null, null))
        .thenThrow(PasswordsDoNotMatchException.class);

  }

  @Test(expectedExceptions = PasswordsDoNotMatchException.class)
  public void changePassword_Failure_NewPasswordsDoNotMatch()
  {

    ReflectionTestUtils.setField(userService, // inject into this object
        "passwordEncoder", // assign to this field
        passwordEncoder); // object to be injected

    Mockito.when(userService.changePassword("", "somePass.1", "somePass.2"))
        .thenThrow(PasswordsDoNotMatchException.class);
  }
}