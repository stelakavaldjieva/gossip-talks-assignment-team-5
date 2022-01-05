package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.user.ChangePasswordRequest;
import bg.codeacademy.spring.gossiptalks.dto.user.CreateUserRequest;
import bg.codeacademy.spring.gossiptalks.dto.user.FollowRequestDTO;
import bg.codeacademy.spring.gossiptalks.dto.user.UserResponseDTO;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserController
{

  private final UserService userService;
  private ResponseEntity<String> successful_operation;

  public UserController(UserService userService)
  {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> listUsers(
      @Min(0) @RequestParam(defaultValue = "0") Integer page,
      @Min(1) @Max(2000) @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "false") boolean f)
  {
    Pageable pageable = PageRequest.of(page, size);

    List<UserResponseDTO> userResponseDTOList = userService.listUsers(name, f, pageable)
        .stream()
        .map(user -> new UserResponseDTO(
            user.getUsername(),
            userService.getCurrentUser().getFriends().contains(user),
            user.getEmail(),
            user.getName()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(userResponseDTOList);

  }//end method

  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> getCurrentUser()
  {

    User user = userService.getCurrentUser();
    UserResponseDTO userResponseDTO = new UserResponseDTO(
        user.getUsername(),
        Boolean.TRUE,
        user.getEmail(),
        user.getName());
    return ResponseEntity.ok(userResponseDTO);

  }//end method

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<String> registerAccount(@Valid @ModelAttribute CreateUserRequest createUserRequest)
  {
    userService.registerAccount
        (createUserRequest.getEmail(),
            createUserRequest.getUsername(),
            createUserRequest.getName(),
            createUserRequest.getPassword(),
            createUserRequest.getPasswordConfirmation());

    return successful_operation;
  }//end method

  @PostMapping(value = "/me", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponseDTO> changePassword(@Valid @ModelAttribute ChangePasswordRequest changePasswordRequest)
  {
    // 1. Get current user and change current user password
    // 1.1. If not user logged throw error 401
    // 2.1. If passwords do not match throw error 400
    User user = userService.changePassword(changePasswordRequest.getOldPassword(),
        changePasswordRequest.getPassword(),
        changePasswordRequest.getPasswordConfirmation());

    // 3. Return proper response
    UserResponseDTO userResponseDTO = new UserResponseDTO(
        user.getUsername(),
        userService.getCurrentUser().getFriends().contains(user),
        user.getEmail(),
        user.getName()
    );

    return ResponseEntity.ok(userResponseDTO);
  }

  @PostMapping(value = "/{username}/follow", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponseDTO> userFollow(@PathVariable String username, @Valid @ModelAttribute
      FollowRequestDTO follow)
  {
    User user = userService.userFollow(username, follow.getFollow());
    UserResponseDTO userResponseDTO = new UserResponseDTO(
        user.getUsername(),
        userService.getCurrentUser().getFriends().contains(user),
        user.getEmail(),
        user.getName()
    );

    return ResponseEntity.ok(userResponseDTO);
  }
}
