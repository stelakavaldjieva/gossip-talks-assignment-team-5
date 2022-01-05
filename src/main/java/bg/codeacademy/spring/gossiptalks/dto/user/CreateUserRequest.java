package bg.codeacademy.spring.gossiptalks.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateUserRequest extends CommonPasswordRequest
{
  @NotBlank(message = "Email cannot be blank")
  @Email
  private String email;

  @Size(max = 32, message = "Username cannot be more than 32 characters long")
  @NotBlank(message = "Username cannot be blank!")
  @Pattern(regexp = "^[a-z0-9.\\-]+$")
  private String username;

  // Full name.
  private String name;


  public String getEmail()
  {
    return email;
  }

  public CreateUserRequest setEmail(String email)
  {
    this.email = email;
    return this;
  }

  public String getUsername()
  {
    return username;
  }

  public CreateUserRequest setUsername(String username)
  {
    this.username = username;
    return this;
  }

  public String getName()
  {
    return name;
  }

  public CreateUserRequest setName(String name)
  {
    this.name = name;
    return this;
  }
}
