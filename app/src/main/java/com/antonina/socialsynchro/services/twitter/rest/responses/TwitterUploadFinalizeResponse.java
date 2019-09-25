package com.antonina.socialsynchro.services.twitter.rest.responses;

import com.google.gson.annotations.SerializedName;

public class TwitterUploadFinalizeResponse extends TwitterResponse {
    @SerializedName("media_id_string")
    private String mediaID;

    @SerializedName("size")
    private long size;

    @SerializedName("expires_after_secs")
    private long expiresAfterSecs;

    @SerializedName("processing_info")
    private ProcessingInfo processingInfo;

    public String getMediaID() {
        return mediaID;
    }

    public ProcessingInfo getProcessingInfo() {
        return processingInfo;
    }

    public static class ProcessingInfo {
        @SerializedName("state")
        private String state;

        @SerializedName("check_after_secs")
        private int checkAfterSecs;

        public String getState() {
            return state;
        }

        public int getCheckAfterSecs() {
            return checkAfterSecs;
        }
    }
}