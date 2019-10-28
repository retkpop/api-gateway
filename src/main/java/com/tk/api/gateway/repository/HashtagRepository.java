package com.tk.api.gateway.repository;

import com.tk.api.gateway.domain.Hashtag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Hashtag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    /**
     *
     * @param slug
     * @return
     */
    Optional<Hashtag> getFirstBySlug(String slug);
}
