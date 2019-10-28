package com.tk.api.gateway.service.googleapi;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;
import com.tk.api.gateway.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleApiService {

    @Autowired
    ApplicationProperties applicationProperties;

    /**
     *
     * @param idVideo
     * @return
     */
    public Optional<VideoListResponse> fetchVideosByQuery(String idVideo) {
        VideoListResponse videoListResponse = new VideoListResponse();
        try {
            YouTube youtube = getYouTube();
            YouTube.Videos.List search = youtube.videos().list("snippet");
            search.setKey(applicationProperties.getGoogleApi().getYoutubeApi().getApiKey());
            search.setId(idVideo);
            search.setPart(applicationProperties.getGoogleApi().getYoutubeApi().getPartName());
            videoListResponse = search.execute();
            System.out.println(videoListResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(videoListResponse);
    }

    /**
     * Instantiates the YouTube object
     */
    public YouTube getYouTube() {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
            (reqeust) -> {}).setApplicationName(applicationProperties.getGoogleApi().getYoutubeApi().getApplicationName()).build();

        return youtube;
    }

}
