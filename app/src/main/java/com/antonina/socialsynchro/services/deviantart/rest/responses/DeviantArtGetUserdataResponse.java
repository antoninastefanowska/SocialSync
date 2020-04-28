package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviantArtGetUserdataResponse extends DeviantArtResponse {
    @SerializedName("features")
    private List<String> features;

    @SerializedName("agreements")
    private List<String> agreements;

    public List<String> getFeatures() {
        return features;
    }

    public List<String> getAgreements() {
        return agreements;
    }
}
