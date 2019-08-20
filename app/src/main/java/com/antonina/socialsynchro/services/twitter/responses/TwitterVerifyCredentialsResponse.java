package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.base.ErrorResponse;
import com.antonina.socialsynchro.base.IErrorResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TwitterVerifyCredentialsResponse implements IErrorResponse {
    @SerializedName("id_str")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("location")
    private String location;

    @SerializedName("description")
    private String description;

    @SerializedName("errors")
    private ArrayList<ErrorResponse> errors;

    public String getID() { return id; }

    public String getName() { return name; }

    public String getScreenName() { return screenName; }

    public String getLocation() { return location; }

    public String getDescription() { return description; }

    @Override
    public String getErrorString() {
        if (errors == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (ErrorResponse error : errors) {
            sb.append(error.getErrorString());
            sb.append('\n');
        }
        return sb.toString();
    }
}
