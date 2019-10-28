package com.tk.api.gateway.service.util;

public class VideoUtil {
    public static String getIdVideoByUrl(String url) {
        String videoId = null;
        try {
            int position = url.indexOf("v=");
            int positionOr = url.indexOf("&");
            if(positionOr > 0) {
                url = url.substring(0, positionOr);
            }
            videoId = url.substring(position+2);
        } catch (Exception e) {
            return null;
        }
        return videoId;
    }
}
