package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.antonina.socialsynchro.common.rest.ErrorResponse;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.google.gson.annotations.SerializedName;

public abstract class FacebookResponse implements IResponse {
    private String undefinedError;

    @SerializedName("error")
    private ErrorResponse error;

    @Override
    public String getErrorString() {
        if (error == null)
            return undefinedError;
        else
            return error.toString();
    }

    @Override
    public void setUndefinedError(String undefinedError) {
        this.undefinedError = undefinedError;
    }
}
