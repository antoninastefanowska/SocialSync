package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.services.ErrorResponse;
import com.google.gson.annotations.SerializedName;

public class TwitterCheckUploadStatusResponse extends TwitterResponse {
    @SerializedName("media_id_string")
    private String mediaID;

    @SerializedName("expires_after_secs")
    private long expiresAfterSecs;

    @SerializedName("processing_info")
    private ProcessingInfo processingInfo;

    @Override
    public String getErrorString() {
        String error = super.getErrorString();
        if (error != null)
            return error;
        else
            return processingInfo.getErrorString();
    }

    public String getMediaID() {
        return mediaID;
    }

    public long getExpiresAfterSecs() {
        return expiresAfterSecs;
    }

    public ProcessingInfo getProcessingInfo() {
        return processingInfo;
    }

    public static class ProcessingInfo {
        @SerializedName("state")
        private String state;

        @SerializedName("check_after_secs")
        private int checkAfterSecs;

        @SerializedName("progress_percent")
        private int progressPercent;

        @SerializedName("error")
        private ErrorResponse error;

        public String getState() {
            return state;
        }

        public int getCheckAfterSecs() {
            return checkAfterSecs;
        }

        public int getProgressPercent() {
            return progressPercent;
        }

        public String getErrorString() {
            return error.toString();
        }
    }
}
