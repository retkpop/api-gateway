package com.tk.api.gateway.service.dto;

import java.util.Arrays;

public class PostDTO {
    private String urlVideo;
    private String title;
    private Long type;
    private Long catId;
    private Object[] hashtag;

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object[] getHashtag() {
        return hashtag;
    }

    public void setHashtag(Object[] hashtag) {
        this.hashtag = hashtag;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
            "urlVideo='" + urlVideo + '\'' +
            ", title='" + title + '\'' +
            ", hashtag=" + Arrays.toString(hashtag) +
            '}';
    }
}
