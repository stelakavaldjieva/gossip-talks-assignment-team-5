package bg.codeacademy.spring.gossiptalks.dto.gossip;

import bg.codeacademy.spring.gossiptalks.validation.CheckForHTML;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateGossipRequest
{

  @NotBlank(message = "You haven't written anything!")
  @CheckForHTML
  @Size(min = 3, max = 500, message = "Size must be between 3 and 500.")
  private String text;

  public String getText()
  {
    return text;
  }

  public CreateGossipRequest setText(String text)
  {
    this.text = text;
    return this;
  }
}
