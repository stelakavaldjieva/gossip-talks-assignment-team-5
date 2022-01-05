package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService
{
  Page<User> listUsers(String name, boolean f, Pageable pageable);

  User getCurrentUser();

  User registerAccount(String email, String username, String name, String password, String passwordConfirmation);

  User changePassword(String oldPassword, String password, String passwordConfirmation);

  User userFollow(String username, Boolean follow);

  User getByUsername(String username);
}
