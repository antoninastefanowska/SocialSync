package com.antonina.socialsynchro.base;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse implements IErrorResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private Integer code;

    public String getMessage() { return message; }

    public Integer getCode() { return code; }

    @Override
    public String getErrorString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" code: ");
        sb.append(Integer.toString(code));
        sb.append(" message: ");
        sb.append(message);
        return sb.toString();
    }
}
