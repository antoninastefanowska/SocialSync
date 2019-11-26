package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookCountResponse extends FacebookResponse {
    @SerializedName("summary")
    private Summary summary;

    public int getTotalCount() {
        return summary.getTotalCount();
    }

    public static class Summary {
        @SerializedName("total_count")
        private int totalCount;

        public int getTotalCount() {
            return totalCount;
        }
    }
}
