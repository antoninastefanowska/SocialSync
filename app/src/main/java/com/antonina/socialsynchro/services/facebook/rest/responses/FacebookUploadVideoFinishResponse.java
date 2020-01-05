package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookUploadVideoFinishResponse extends FacebookResponse {
    @SerializedName("success")
    private boolean success;

    public boolean getSuccess() {
        return success;
    }
}
