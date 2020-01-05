package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.antonina.socialsynchro.common.rest.IResponse;
import com.google.gson.annotations.SerializedName;

public abstract class DeviantArtResponse implements IResponse {
    private String undefinedError;

    @SerializedName("error_description")
    private String errorDescription;

    @Override
    public String getErrorString() {
        if (errorDescription == null)
            return undefinedError;
        else
            return errorDescription;
    }

    @Override
    public void setUndefinedError(String undefinedError) {
        this.undefinedError = undefinedError;
    }
}
