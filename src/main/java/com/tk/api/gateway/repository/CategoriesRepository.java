package com.tk.api.gateway.repository;

import com.tk.api.gateway.domain.Categories;
import com.tk.api.gateway.domain.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Categories entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    @Query(value = "select distinct posts from Categories cat inner join cat.posts posts where cat.id = :id")
    List<Posts> getPostByIdCat(Pageable pageable, Long id);
}
