package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.exception.NotSubscribedException;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GossipServiceImp implements GossipService
{

  private final GossipRepository gossipRepository;
  private final UserService      userService;

  public GossipServiceImp(GossipRepository gossipRepository,
                          UserService userService)
  {
    this.gossipRepository = gossipRepository;
    this.userService = userService;
  }

  @Override
  public Page<Gossip> getByUser(String username, Pageable pageable)
  {
    //You can see user's gossip if you follow it
    if (userService.getCurrentUser()
        .getFriends()
        .contains(userService.getByUsername(username))
        ||  //Or if you want to see your gossips
        userService.getCurrentUser().getUsername().equals(username)
    ) {
      return gossipRepository.findByUserUsernameOrderByDatetimeDesc(
          username, pageable);
    }
    else {
      throw new NotSubscribedException();
    }
  }

  @Override
  public Page<Gossip> getAllSubscriberGossips(Pageable pageable)
  {
    return gossipRepository.findFollowingUsers(userService.getCurrentUser().getId(), pageable);
  }

  @Override
  public Gossip createGossip(String text)
  {
    Gossip gossip = new Gossip();
    gossip.setText(text);
    gossip.setDatetime(LocalDateTime.now());
    gossip.setUser(userService.getCurrentUser());
    return gossipRepository.save(gossip);
  }
}
