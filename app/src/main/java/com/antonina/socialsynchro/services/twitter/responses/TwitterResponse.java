package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.services.ErrorResponse;
import com.antonina.socialsynchro.services.IResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public abstract class TwitterResponse implements IResponse {
    private String undefinedError;

    @SerializedName("errors")
    private ArrayList<ErrorResponse> errors;

    @Override
    public String getErrorString() {
        if (errors == null)
            return undefinedError;
        StringBuilder sb = new StringBuilder();
        for (ErrorResponse error : errors) {
            sb.append(error.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public void setUndefinedError(String undefinedError) {
        this.undefinedError = undefinedError;
    }
}
