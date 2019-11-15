package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FacebookGetPagePictureResponse extends FacebookResponse {
    @SerializedName("data")
    private Data data;

    public class Data {
        @SerializedName("url")
        private String url;

        public String getURL() {
            return url;
        }
    }

    public String getURL() {
        return data.getURL();
    }
}
