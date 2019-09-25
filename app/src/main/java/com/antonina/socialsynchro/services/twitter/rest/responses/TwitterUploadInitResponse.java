package com.antonina.socialsynchro.services.twitter.rest.responses;

import com.google.gson.annotations.SerializedName;

public class TwitterUploadInitResponse extends TwitterResponse {
    @SerializedName("media_id_string")
    private String mediaID;

    public String getMediaID() {
        return mediaID;
    }
}
