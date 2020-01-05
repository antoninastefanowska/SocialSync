package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookUploadVideoTransferResponse extends FacebookResponse {
    @SerializedName("start_offset")
    private long startOffset;

    @SerializedName("end_offset")
    private long endOffset;

    public long getStartOffset() {
        return startOffset;
    }

    public long getEndOffset() {
        return endOffset;
    }
}
