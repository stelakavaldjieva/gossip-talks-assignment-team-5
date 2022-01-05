package bg.codeacademy.spring.gossiptalks.dto.user;

import javax.validation.constraints.NotBlank;

public class ChangePasswordRequest extends CommonPasswordRequest
{
  @NotBlank(message = "Password is mandatory!")
  private String oldPassword;


  public String getOldPassword()
  {
    return oldPassword;
  }

  public ChangePasswordRequest setOldPassword(String oldPassword)
  {
    this.oldPassword = oldPassword;
    return this;
  }

}
