package com.nhnacademy.codequestweb.response.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponseDto {
    @JsonProperty("isFolder")
    private boolean isFolder;
    @JsonProperty("id")
    private String id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("name")
    private String name;
    @JsonProperty("path")
    private String path;
    @JsonProperty("bytes")
    private long bytes;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("operationId")
    private String operationId;
    @JsonProperty("imageProperty")
    private ImageProperty imageProperty;
    @JsonProperty("queues")
    private List<Queue> queues;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageProperty {
        @JsonProperty("width")
        private int width;
        @JsonProperty("height")
        private int height;
        @JsonProperty("createdAt")
        private String createdAt;
        @JsonProperty("coordinate")
        private Coordinate coordinate;

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Coordinate {
            @JsonProperty("lat")
            private Double lat;
            @JsonProperty("lng")
            private Double lng;
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Queue {
        @JsonProperty("queueId")
        private String queueId;
        @JsonProperty("queueType")
        private String queueType;
        @JsonProperty("status")
        private String status;
        @JsonProperty("tryCount")
        private int tryCount;
        @JsonProperty("queuedAt")
        private String queuedAt;
        @JsonProperty("operationId")
        private String operationId;
        @JsonProperty("url")
        private String url;
        @JsonProperty("name")
        private String name;
        @JsonProperty("path")
        private String path;
    }
}
