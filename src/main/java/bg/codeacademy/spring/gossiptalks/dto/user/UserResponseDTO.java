package bg.codeacademy.spring.gossiptalks.dto.user;

public class UserResponseDTO
{

  private String username;

  private Boolean following;

  private String email;

  // Full name.
  private String name;

  public UserResponseDTO()
  {
  }

  public UserResponseDTO(String username, Boolean following, String email, String name)
  {
    this.username = username;
    this.following = following;
    this.email = email;
    this.name = name;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public Boolean getFollowing()
  {
    return following;
  }

  public void setFollowing(Boolean following)
  {
    this.following = following;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
