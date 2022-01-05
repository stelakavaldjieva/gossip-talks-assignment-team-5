package bg.codeacademy.spring.gossiptalks.dto.user;

import bg.codeacademy.spring.gossiptalks.validation.StrongPassword;

import javax.validation.constraints.NotBlank;

public class CommonPasswordRequest
{
  @NotBlank(message = "Password is mandatory!")
  @StrongPassword
  private String password;

  @NotBlank(message = "Password is mandatory!")
  private String passwordConfirmation;

  public String getPassword()
  {
    return password;
  }

  public CommonPasswordRequest setPassword(String password)
  {
    this.password = password;
    return this;
  }

  public String getPasswordConfirmation()
  {
    return passwordConfirmation;
  }

  public CommonPasswordRequest setPasswordConfirmation(String passwordConfirmation)
  {
    this.passwordConfirmation = passwordConfirmation;
    return this;
  }
}
