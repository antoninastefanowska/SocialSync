package com.antonina.socialsynchro.services;

public interface IRawResponse {
    void createFromString(String stringResponse);
    void createFromErrorString(String errorResponse);
}
