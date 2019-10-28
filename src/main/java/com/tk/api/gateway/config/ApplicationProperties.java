package com.tk.api.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Api Gateway.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final Redis redis = new Redis();

    public Redis getRedis() {
        return redis;
    }

    public static  class Redis {
        private String url;
        private long timeToLiveMinutes;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getTimeToLiveMinutes() {
            return timeToLiveMinutes;
        }

        public void setTimeToLiveMinutes(long timeToLiveMinutes) {
            this.timeToLiveMinutes = timeToLiveMinutes;
        }
    }

    private final GoogleApi googleApi = new GoogleApi();

    public GoogleApi getGoogleApi() {
        return googleApi;
    }

    public static class GoogleApi {
        private YoutubeApi youtubeApi = new YoutubeApi();

        public YoutubeApi getYoutubeApi() {
            return youtubeApi;
        }
        public static class YoutubeApi {
            private String apiKey;
            private String partName;
            private String applicationName;

            public String getApplicationName() {
                return applicationName;
            }

            public void setApplicationName(String applicationName) {
                this.applicationName = applicationName;
            }

            public String getPartName() {
                return partName;
            }

            public void setPartName(String partName) {
                this.partName = partName;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }
        }
    }
}
