package bg.codeacademy.spring.gossiptalks.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Gossip
{

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty(message = "You haven't written anything!")
  @Size(min = 3, max = 500, message = "The gossip must be min 3 symbols and max 500!")
  private String text;

  @ManyToOne
  private User user;

  @Column(nullable = false)
  private LocalDateTime datetime;

  public Gossip()
  {
  }

  public Gossip(Long id, String text, User user)
  {
    this.id = id;
    this.text = text;
    this.user = user;
    this.datetime = LocalDateTime.now();
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
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

  public User getUser()
  {
    return user;
  }

  public void setUser(User user)
  {
    this.user = user;
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
