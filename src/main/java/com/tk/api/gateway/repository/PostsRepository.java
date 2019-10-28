package com.tk.api.gateway.repository;

import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Posts entity.
 */
@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    Optional<Posts> findFirstBySlug(@Param("slug") String slug);


    @Query( "select posts from Posts posts where id in :id" )
    List<Posts> findAllViewedById(@Param("id") List<Long> listId, Pageable pageable);

    @Query(value = "select distinct posts from Posts posts left join fetch posts.hashtags",
        countQuery = "select count(distinct posts) from Posts posts")
    Page<Posts> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct posts from Posts posts left join fetch posts.hashtags")
    List<Posts> findAllWithEagerRelationships();

    @Query("select posts from Posts posts left join fetch posts.hashtags where posts.id =:id")
    Optional<Posts> findOneWithEagerRelationships(@Param("id") Long id);

    /**
     *
     * @param idVideo
     * @param title
     * @return
     */
    @Query("select count(p.idVideo) from Posts p where p.idVideo = :idVideo or p.title = :title")
    int findOnePostIdVideoAndTitle( @Param("idVideo") String idVideo, @Param("title") String title);

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findCustomPost(Pageable pageable);

    @Query("SELECT p FROM Posts p ORDER BY p.views DESC")
    List<Posts> getVideoPopular(Pageable pageable);

    @Query("SELECT posts FROM Posts posts WHERE posts.user = :listId")
    List<Posts> findAllByUse(@Param("listId") List<User> listId, Pageable pageable);

}
