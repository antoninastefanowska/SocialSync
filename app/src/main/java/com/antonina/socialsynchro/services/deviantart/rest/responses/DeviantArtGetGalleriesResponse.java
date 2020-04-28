package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviantArtGetGalleriesResponse extends DeviantArtResponse {
    @SerializedName("results")
    private List<GalleryResponse> galleries;

    @SerializedName("has_more")
    private boolean hasMore;

    @SerializedName("next_offset")
    private Integer nextOffset;

    public List<GalleryResponse> getGalleries() {
        return galleries;
    }

    public boolean getHasMore() {
        return hasMore;
    }

    public Integer getNextOffset() {
        return nextOffset;
    }

    public static class GalleryResponse extends DeviantArtResponse {
        @SerializedName("folderid")
        private String id;

        @SerializedName("name")
        private String name;

        public String getID() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
