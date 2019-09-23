package com.antonina.socialsynchro.services.twitter.responses;

import com.google.gson.annotations.SerializedName;

public class TwitterVerifyCredentialsResponse extends TwitterResponse {
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

    public String getID() { return id; }

    public String getName() { return name; }

    public String getScreenName() { return screenName; }

    public String getLocation() { return location; }

    public String getDescription() { return description; }
}
