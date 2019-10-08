package com.antonina.socialsynchro.services.backend.responses;

import com.antonina.socialsynchro.common.rest.IResponse;
import com.google.gson.annotations.SerializedName;

public abstract class BackendResponse implements IResponse {
    private String undefinedError;

    @SerializedName("message")
    private String message;

    @Override
    public String getErrorString() {
        if (message == null)
            return undefinedError;
        else
            return message;
    }

    @Override
    public void setUndefinedError(String undefinedError) {
        this.undefinedError = undefinedError;
    }
}
