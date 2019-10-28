package com.tk.api.gateway.service.posts;

import com.google.api.services.youtube.model.VideoListResponse;
import com.tk.api.gateway.domain.Categories;
import com.tk.api.gateway.domain.Hashtag;
import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.exception.ResourceNotFoundException;
import com.tk.api.gateway.repository.CategoriesRepository;
import com.tk.api.gateway.repository.HashtagRepository;
import com.tk.api.gateway.repository.UserRepository;
import com.tk.api.gateway.security.SecurityUtils;
import com.tk.api.gateway.service.dto.PostDTO;
import com.tk.api.gateway.service.util.Slug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
public class PostsService {

    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoriesRepository categoriesRepository;


    private final Logger log = LoggerFactory.getLogger(PostsService.class);

    public Optional<Posts> generateDataPost(VideoListResponse videoListResponse, PostDTO postDTO) {
        log.debug("Data request videoListResponse {}", videoListResponse);
        Posts posts = new Posts();
        //Set user post
        Optional<String> username = SecurityUtils.getCurrentUserLogin();
        userRepository.findOneByLogin(username.get()).map(user -> {
            posts.setTitle(postDTO.getTitle());
            posts.setSlug(Slug.makeSlug(postDTO.getTitle()));
            posts.setExcerpt(videoListResponse.getItems().get(0).getSnippet().getDescription());
            posts.setDescription(videoListResponse.getItems().get(0).getSnippet().getDescription());
            posts.setThumbnail(videoListResponse.getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl());
            posts.setType(postDTO.getType());
            posts.setViews(0L);
            posts.setIdVideo(videoListResponse.getItems().get(0).getId());
            posts.setCreatedDate(ZonedDateTime.now());
            posts.setUser(user);
            //Mapping category
            Optional<Categories> categories = categoriesRepository.findById(postDTO.getCatId());
            posts.setCategories(categories.get());
            //Add hashtag
            if(postDTO.getHashtag() != null) {
                for (final Object tag : postDTO.getHashtag()) {
                    try {
                        String tagN = tag.toString();
                        String slug = Slug.makeSlug(tagN);
                        Optional<Hashtag> getHashtag = hashtagRepository.getFirstBySlug(slug);
                        if (!slug.isEmpty()) {
                            if (getHashtag.isPresent()) {
                                Hashtag hashtag = new Hashtag(getHashtag.get().getId(), tagN, slug);
                                posts.getHashtags().add(hashtag);
                            } else {
                                Hashtag hashtag = new Hashtag(tagN, slug);
                                //TODO @many to many add database
//                            posts.getHashtags().add(hashtag);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }
            return posts;
        }).orElseThrow(() -> new ResourceNotFoundException("User not exits! Get user by " + username.get() + " not found"));
        return Optional.of(posts);
    }
}
