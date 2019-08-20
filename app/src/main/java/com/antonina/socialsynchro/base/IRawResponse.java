package com.antonina.socialsynchro.base;

public interface IRawResponse {
    void createFromString(String stringResponse);
    void createFromErrorString(String errorResponse);
}
