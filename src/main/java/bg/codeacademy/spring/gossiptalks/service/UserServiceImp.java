package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.exception.*;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService
{

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder)
  {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User registerAccount(String email, String username, String name, String password,
                              String passwordConfirmation)
  {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new UserAlreadyExistsException();
    }

    if (!password.equals(passwordConfirmation)) {
      throw new PasswordsDoNotMatchException();
    }

    User user = new User();
    user.setEmail(email);
    user.setUsername(username);
    user.setName(name);
    user.setPassword(passwordEncoder.encode(password));
    return userRepository.save(user);
  }

  @Override
  public User changePassword(String oldPassword, String password, String passwordConfirmation)
  {
    // Object.equals = not null check
    if (!Objects.equals(password, passwordConfirmation)) {
      throw new PasswordsDoNotMatchException();
    }

    User user = getCurrentUser();

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new PasswordsDoNotMatchException("Not your current password!");
    }

    user.setPassword(passwordEncoder.encode(password));
    return userRepository.save(user);
  }

  @Override
  public User userFollow(String username, Boolean follow)
  {

    //Check if current user wants to subscribe itself.
    if (username.equals(getCurrentUser().getUsername())) {
      throw new UserSubscribeItselfException();
    }
    //Get current user's friends.
    Set<User> currentUserFriends = getCurrentUser().getFriends();

    //Get user for subscribing.
    User wantedUser = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    //Get the wanted user from current user's friends if exists, else isInFriends=false
    boolean isInFriends = currentUserFriends.contains(wantedUser);

    //When he want to follow T but it is in the list already T - booleans are equals, nothing should happen.
    //When he want to unfollow F but it is not the list F - booleans are equals, nothing should happen.
    if (follow == isInFriends) {
      return wantedUser;
      //Want to follow T. Is in list is not T because of above if.
    }
    else if (follow) {
      currentUserFriends.add(wantedUser);
    }
    //Want to unfollow F. Is in list is not F because of above if.
    else {
      currentUserFriends.remove(wantedUser);
    }
    userRepository.save(getCurrentUser());
    return wantedUser;
  }

  @Override
  public User getByUsername(String username)
  {
    return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public Page<User> listUsers(String name, boolean f, Pageable pageable)
  {
    //When the current user wants all users, without searching filter.
    if (name == null) {
      //(1) Get only following.
      if (f) {
        return userRepository.findCurrentUserFriends(getCurrentUser().getId(), pageable);
        //(2) Get all users.
      }
      else {
        return userRepository.findAllByOrderByGossipsCountDesc(pageable);
      }
      //When the current user wants all users
    }
    else {
      //(3) Search in followings only.
      if (f) {
        return userRepository.findCurrentUserFriendsSearchingName(getCurrentUser().getId(), name, pageable);
        //(4) Search in all users.
      }
      else {
        return userRepository.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(name, name, pageable);
      }
    }
  }//end method

  @Override
  public User getCurrentUser()
  {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken) {
      throw new NoAuthenticatedUserException();
    }
    else {
      String username = authentication.getName();
      return userRepository.findByUsername(username)
          .orElseThrow(UserNotFoundException::new);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {

    User user = userRepository.findByUsername(username)
        .orElseThrow(UserNotFoundException::new);

    return org.springframework.security.core.userdetails.User.withUsername(username)
        .password(user.getPassword())
        .roles("USER")
        .build();
  }
}
