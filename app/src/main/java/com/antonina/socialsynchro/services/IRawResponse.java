package com.antonina.socialsynchro.services;

public interface IRawResponse extends IResponse {
    void createFromString(String stringResponse);
    void createFromErrorString(String errorResponse);
}
