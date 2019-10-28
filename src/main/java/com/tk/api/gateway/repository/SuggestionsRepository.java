package com.tk.api.gateway.repository;

import com.tk.api.gateway.domain.Suggestions;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Suggestions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuggestionsRepository extends JpaRepository<Suggestions, Long> {

    Optional<Suggestions> findFirstBySlug(@Param("slug") String slug);

    @Query("SELECT s FROM Suggestions s ORDER BY s.sort ASC")
    List<Suggestions> findCustomSuggestions(Pageable pageable);
}
