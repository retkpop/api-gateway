package com.tk.api.gateway.web.rest;

import com.google.api.services.youtube.model.VideoListResponse;
import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.domain.User;
import com.tk.api.gateway.repository.PostsRepository;
import com.tk.api.gateway.security.AuthoritiesConstants;
import com.tk.api.gateway.service.UserService;
import com.tk.api.gateway.service.dto.IdSubscribes;
import com.tk.api.gateway.service.dto.PostDTO;
import com.tk.api.gateway.service.googleapi.GoogleApiService;
import com.tk.api.gateway.service.posts.PostsService;
import com.tk.api.gateway.service.util.VideoUtil;
import com.tk.api.gateway.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.tk.api.gateway.domain.Posts}.
 */
@RestController
@RequestMapping("/api")
public class PostsResource {

    private final Logger log = LoggerFactory.getLogger(PostsResource.class);

    private static final String ENTITY_NAME = "posts";


    @Autowired
    GoogleApiService googleApiService;

    @Autowired
    PostsService postsService;


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostsRepository postsRepository;

    private final UserService userService;

    public PostsResource(PostsRepository postsRepository, UserService userService) {
        this.postsRepository = postsRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /posts} : Create a new posts.
     *
     * @param posts the posts to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new posts, or with status {@code 400 (Bad Request)} if the posts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posts")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Posts> createPosts(@RequestBody Posts posts) throws URISyntaxException {
        log.debug("REST request to save Posts : {}", posts);
        if (posts.getId() != null) {
            throw new BadRequestAlertException("A new posts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Posts result = postsRepository.save(posts);
        return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code POST  /posts} : Create a new posts.
     *
     * @param posts the posts to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new posts, or with status {@code 400 (Bad Request)} if the posts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posts/add-video")
    @Secured("ROLE_USER")
    public ResponseEntity<Posts> createPosts(@RequestBody PostDTO posts) throws URISyntaxException {
        log.debug("REST request to save Posts : {}", posts);
        if (posts.getUrlVideo() == null) {
            throw new BadRequestAlertException("URL VIDEO NOT NULL", ENTITY_NAME, "URL_NULL");
        }
        String idVideo = VideoUtil.getIdVideoByUrl(posts.getUrlVideo());
        int countPost = postsRepository.findOnePostIdVideoAndTitle(idVideo, posts.getTitle());
        if(countPost > 0) {
            throw new BadRequestAlertException("URL OR TITILE VIDEO EXIST", ENTITY_NAME, "POST_EXIST");
        }
        Optional<VideoListResponse> videoListResponse = googleApiService.fetchVideosByQuery(idVideo);
        if(videoListResponse.isPresent()) {
            Optional<Posts> postGenerate = postsService.generateDataPost(videoListResponse.get(), posts);
            Posts result = postsRepository.save(postGenerate.get());
            return ResponseEntity.created(new URI("/api/posts/add-video/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        }
        throw new BadRequestAlertException("URL VIDEO NOT FOUND", ENTITY_NAME, "URL_NOT_FOUND");
    }

    /**
     * {@code PUT  /posts} : Updates an existing posts.
     *
     * @param posts the posts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posts,
     * or with status {@code 400 (Bad Request)} if the posts is not valid,
     * or with status {@code 500 (Internal Server Error)} if the posts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posts")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Posts> updatePosts(@RequestBody Posts posts) throws URISyntaxException {
        log.debug("REST request to update Posts : {}", posts);
        if (posts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Posts result = postsRepository.save(posts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posts.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GetMapping("/posts")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public List<Posts> getAllPosts(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Posts");
        return postsRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /posts/:id} : get the "id" posts.
     *
     * @param id the id of the posts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the posts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posts/{id}")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public ResponseEntity<Posts> getPosts(@PathVariable Long id) {
        log.debug("REST request to get Posts : {}", id);
        Optional<Posts> posts = postsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(posts);
    }

    /**
     * {@code GET  /posts/:id} : get the "id" posts.
     *
     * @param slug the id of the posts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the posts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posts/get-video-by-slug/{slug}")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public ResponseEntity<Posts> getVideoBySlug(@PathVariable String slug) {
        log.debug("REST request to get Posts : {}", slug);
        Optional<Posts> posts = postsRepository.findFirstBySlug(slug);
        return ResponseUtil.wrapOrNotFound(posts);
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" posts.
     *
     * @param id the id of the posts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posts/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deletePosts(@PathVariable Long id) {
        log.debug("REST request to delete Posts : {}", id);
        postsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     *
     * @param link
     * @return
     * @throws Exception
     */
    @PostMapping("/posts/check-link")
    @Secured("ROLE_USER")
    public ResponseEntity<VideoListResponse> getPosts(@RequestBody String link) throws Exception {
        log.debug("REST request to get Posts : {}", link);
        String idVideo = VideoUtil.getIdVideoByUrl(link);
        Optional<VideoListResponse> videoListResponse = googleApiService.fetchVideosByQuery(idVideo);
        return ResponseUtil.wrapOrNotFound(videoListResponse);
    }

    /**
     *
     * @param type
     * @param limit
     * @return
     */
    @GetMapping("/posts/get-post-custom/{type}/{limit}")
    public List<Posts> getPostsCustom(@PathVariable String type, @PathVariable int limit) {
        log.debug("REST request to custom get Posts");
        List<Posts> posts = new ArrayList<>();
        if(type.compareTo("new") == 0) {
            posts = postsRepository.findCustomPost(new PageRequest(0, limit));
        }
        return posts;
    }

    /**
     *
     * @param listId
     * @return
     * @throws Exception
     */
    @PostMapping("/posts/video-viewed")
    @Secured("ROLE_USER")
    public List<Posts> getListVideoViewed(@RequestBody List<Long> listId) throws Exception {
        List<Posts> posts = new ArrayList<>();
        try {
            log.debug("REST request to getListVideoViewed : {}", listId);
            posts = postsRepository.findAllViewedById(listId, new PageRequest(0, 20));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/posts/add-view-post")
    public ResponseEntity<Void> updateViewPost(@RequestBody Long id) throws Exception {
        try {
            Optional<Posts> posts = postsRepository.findById(id);
            if(posts.isPresent()) {
                int views = posts.get().getViews().intValue() + 1;
                posts.get().setViews((long) views);
                postsRepository.save(posts.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping("/posts/get-video-popular/{page}/{size}")
    public List<Posts> getVideoPopular(@PathVariable int page, @PathVariable int size) throws Exception {
        List<Posts> posts = new ArrayList<>();
        try {
            log.debug("REST request to getVideoPopular:");
            posts = postsRepository.getVideoPopular(new PageRequest(page, size));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
    /**
     *
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping("/posts/get-video-subscribes/{page}/{size}")
    public List<Posts> getVideoSubscribes(@PathVariable int page, @PathVariable int size) throws Exception {
        List<Posts> posts = new ArrayList<>();
        try {
            log.debug("REST request to getVideoSubscribes:");
            List<Long> users = userService.getListUserSubscribed();
            List<User> users1 = userService.getAllUserByListId(users);
            posts = postsRepository.findAllByUse(users1, new PageRequest(page, size));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
}

