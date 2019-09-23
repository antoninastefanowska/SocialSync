package com.antonina.socialsynchro.services;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"NullableProblems", "StringBufferReplaceableByString"})
public class ErrorResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private Integer code;

    public String getMessage() { return message; }

    public Integer getCode() { return code; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" code: ");
        sb.append(code);
        sb.append(" message: ");
        sb.append(message);
        return sb.toString();
    }
}
