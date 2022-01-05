package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  @Query(value = "SELECT U.*, COUNT(G.ID) AS GCOUNT FROM USER U "
      + "LEFT JOIN GOSSIP G ON G.USER_ID = U.ID "
      + "GROUP BY U.ID "
      + "ORDER BY GCOUNT DESC, U.USERNAME",
      countQuery = "SELECT count(*) FROM USER",
      nativeQuery = true)
  Page<User> findAllByOrderByGossipsCountDesc(Pageable pageable);

  Page<User> findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(String search, String search2,
                                                                          Pageable pageable);

  @Query(value = "SELECT * FROM USER U "
      + "INNER JOIN USER_FRIENDS UF ON UF.FRIENDS_ID = U.ID "
      + "WHERE UF.USER_ID = ?1 "
      + "GROUP BY U.ID "
      + "ORDER BY U.USERNAME",
      countQuery = "SELECT count(*) FROM USER",
      nativeQuery = true)
  Page<User> findCurrentUserFriends(Long id, Pageable pageable);

  @Query(value = "SELECT * FROM USER U "
      + "INNER JOIN USER_FRIENDS UF ON UF.FRIENDS_ID = U.ID "
      + "WHERE UF.USER_ID = ?1 "
      + "AND (UPPER(USERNAME) LIKE UPPER(CONCAT('%',?2,'%')) "
      + "OR UPPER(NAME) LIKE UPPER(CONCAT('%',?2,'%'))) "
      + "GROUP BY U.ID "
      + "ORDER BY U.USERNAME",
      countQuery = "SELECT count(*) FROM USER",
      nativeQuery = true)
  Page<User> findCurrentUserFriendsSearchingName(Long Id, String search, Pageable pageable);
}
