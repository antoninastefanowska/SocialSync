package com.antonina.socialsynchro.common.rest;

public interface IRawResponse extends IResponse {
    void createFromString(String stringResponse);
    void createFromErrorString(String errorResponse);
}
