package com.tk.api.gateway.service.dto;

import java.util.ArrayList;

public class SuggestionsDTO {
    private Long posts_id;
    private ArrayList<String> suggestionss;

    public Long getPosts_id() {
        return posts_id;
    }

    public void setPosts_id(Long posts_id) {
        this.posts_id = posts_id;
    }

    public ArrayList<String> getSuggestionss() {
        return suggestionss;
    }

    public void setSuggestionss(ArrayList<String> suggestionss) {
        this.suggestionss = suggestionss;
    }

    @Override
    public String toString() {
        return "SuggestionsDTO{" +
            "posts_id=" + posts_id +
            ", suggestionss=" + suggestionss +
            '}';
    }
}
