package bg.codeacademy.spring.gossiptalks.dto.user;

import com.sun.istack.NotNull;

public class FollowRequestDTO
{

  @NotNull
  private Boolean follow;

  public FollowRequestDTO()
  {

  }

  public FollowRequestDTO(Boolean follow)
  {
    this.follow = follow;
  }

  public Boolean getFollow()
  {
    return follow;
  }

  public void setFollow(Boolean follow)
  {
    this.follow = follow;
  }
}
