package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookUploadVideoStartResponse extends FacebookResponse {
    @SerializedName("upload_session_id")
    private String sessionID;

    @SerializedName("video_id")
    private String videoID;

    @SerializedName("start_offset")
    private long startOffset;

    @SerializedName("end_offset")
    private long endOffset;

    public String getSessionID() {
        return sessionID;
    }

    public String getVideoID() {
        return videoID;
    }

    public long getStartOffset() {
        return startOffset;
    }

    public long getEndOffset() {
        return endOffset;
    }
}
