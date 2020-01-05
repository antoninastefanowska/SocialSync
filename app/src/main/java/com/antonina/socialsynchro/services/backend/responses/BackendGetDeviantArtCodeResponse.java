package com.antonina.socialsynchro.services.backend.responses;

import com.google.gson.annotations.SerializedName;

public class BackendGetDeviantArtCodeResponse extends BackendResponse {
    @SerializedName("state")
    private String state;

    @SerializedName("code")
    private String code;

    public String getState() {
        return state;
    }

    public String getCode() {
        return code;
    }
}
