package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GossipService
{

  Page<Gossip> getByUser(String username, Pageable pageable);

  Page<Gossip> getAllSubscriberGossips(Pageable pageable);

  Gossip createGossip(String text);
}
