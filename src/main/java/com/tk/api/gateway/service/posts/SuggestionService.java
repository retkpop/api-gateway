package com.tk.api.gateway.service.posts;

import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.domain.Suggestions;
import com.tk.api.gateway.repository.PostsRepository;
import com.tk.api.gateway.repository.SuggestionsRepository;
import com.tk.api.gateway.service.dto.SuggestionsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SuggestionService {
    @Autowired
    SuggestionsRepository suggestionsRepository;

    @Autowired
    PostsRepository postsRepository;

    public Optional<Suggestions> generateDataPost(SuggestionsDTO suggestionsDTO) {
        Optional<Suggestions> suggestions = Optional.of(new Suggestions());
        for (final String tag : suggestionsDTO.getSuggestionss()) {
            suggestions = suggestionsRepository.findById(Long.valueOf(tag));
            Posts temp = suggestions.get().getPosts().stream().filter(post -> suggestionsDTO.getPosts_id().equals(post.getId())).findAny().orElse(null);
            if(temp == null) {
                Optional<Posts> posts = postsRepository.findById(Long.valueOf(suggestionsDTO.getPosts_id()));
                suggestions.get().addPosts(posts.get());
            }
        }
        return suggestions;
    }
}
