package bg.codeacademy.spring.gossiptalks.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
public class User
{

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank(message = "Email cannot be blank")
  @Email
  @Column(unique = true)
  private String email;

  @Size(max = 32, message = "Username cannot be more than 32 characters long")
  @NotBlank(message = "Username cannot be blank!")
  @Pattern(regexp = "^[a-z0-9.\\-]+$")
  @Column(unique = true, nullable = false)
  private String username;

  // Full name.
  private String name;

  @NotBlank(message = "Password is mandatory!")
  private String password;

  @ManyToMany
  private Set<User> friends;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Gossip> gossips;

  public User()
  {
  }

  public User(Long id, String username, String name, String password)
  {
    this.id = id;
    this.username = username;
    this.name = name;
    this.password = password;
  }

  public Long getId()
  {
    return id;
  }

  public User setId(Long id)
  {
    this.id = id;
    return this;
  }

  public String getUsername()
  {
    return username;
  }

  public User setUsername(String username)
  {
    this.username = username;
    return this;
  }

  public String getEmail()
  {
    return email;
  }

  public User setEmail(String email)
  {
    this.email = email;
    return this;
  }

  public String getName()
  {
    return name;
  }

  public User setName(String fullName)
  {
    this.name = fullName;
    return this;
  }

  public String getPassword()
  {
    return password;
  }

  public User setPassword(String password)
  {
    this.password = password;
    return this;
  }

  public Set<User> getFriends()
  {
    return friends;
  }

  public User setFriends(Set<User> friends)
  {
    this.friends = friends;
    return this;
  }

  public List<Gossip> getGossips()
  {
    return gossips;
  }

  public void setGossips(List<Gossip> gossips)
  {
    this.gossips = gossips;
  }
}
