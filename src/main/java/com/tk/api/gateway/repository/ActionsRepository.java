package com.tk.api.gateway.repository;

import com.tk.api.gateway.domain.Actions;
import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Actions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionsRepository extends JpaRepository<Actions, Long> {

    List<Actions> findAllByUserAndPosts(User user, Posts posts);
    List<Actions> findAllByPosts(Posts posts);
}
