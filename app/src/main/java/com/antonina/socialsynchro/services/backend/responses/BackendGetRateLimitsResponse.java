package com.antonina.socialsynchro.services.backend.responses;

import com.google.gson.annotations.SerializedName;

public class BackendGetRateLimitsResponse extends BackendResponse {
    @SerializedName("remaining")
    private int remaining;

    @SerializedName("reset")
    private long reset;

    public int getRemaining() {
        return remaining;
    }

    public long getReset() {
        return reset;
    }
}
