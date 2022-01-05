package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GossipRepository extends JpaRepository<Gossip, String>
{

  Page<Gossip> findByUserUsernameOrderByDatetimeDesc(String username, Pageable pageable);

  @Query(value = "SELECT * FROM GOSSIP G "
      + "INNER JOIN USER_FRIENDS U ON G.USER_ID = U.FRIENDS_ID "
      + "WHERE U.USER_ID = :id "
      + "ORDER BY G.DATETIME DESC, G.ID DESC",
      countQuery = "SELECT count(*) FROM GOSSIP",
      nativeQuery = true)
  Page<Gossip> findFollowingUsers(@Param("id") Long id, Pageable pageable);
}
