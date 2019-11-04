package com.tk.api.gateway.repository;

import com.tk.api.gateway.domain.User;

import com.tk.api.gateway.service.dto.IdSubscribes;
import com.tk.api.gateway.service.dto.UserDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);


    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);


    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    /**
     *
     * @param subscribeId
     * @param userId
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM tbl_subscribes as subscribe WHERE (subscribe.person_id = :userId AND subscribe.subscribe_id = :subscribeId) ", nativeQuery = true)
    int findUsersBySubscribeOfAndSubscribes(@Param("subscribeId") Long subscribeId, @Param("userId") Long userId);

    @Query(value = "SELECT tbl_subscribes.subscribe_id FROM tbl_subscribes  WHERE(tbl_subscribes.person_id = :userId)", nativeQuery = true)
    List<Long> findUsersBySubscribeOf(@Param("userId") Long userId);

    @Query( "select user from User user where id in :ids" )
    List<User> findAllByListId(@Param("ids") List<Long> listId);

    @Query(value = "SELECT count(*) FROM tbl_subscribes  WHERE(tbl_subscribes.subscribe_id = :id)", nativeQuery = true)
    int countNumberSubscribeUser(@Param("id") Long id);

    @Query(value = "select * from jhi_user as u inner join tbl_subscribes as s on s.subscribe_id = u.id where s.person_id = :userId", nativeQuery = true)
    List<User> findAllBySubscribed(@Param("userId") Long userId);
}
