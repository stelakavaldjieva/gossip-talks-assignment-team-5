package bg.codeacademy.spring.gossiptalks.dto.gossip;

import java.time.LocalDateTime;

public class GossipDTO
{

  private String id;

  private String text;

  private String username;

  private LocalDateTime datetime;

  public GossipDTO(String id, String text, String username, LocalDateTime datetime)
  {
    this.id = id;
    this.text = text;
    this.username = username;
    this.datetime = datetime;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }


  public LocalDateTime getDatetime()
  {
    return datetime;
  }

  public void setDatetime(LocalDateTime datetime)
  {
    this.datetime = datetime;
  }
}
