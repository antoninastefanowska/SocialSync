package com.antonina.socialsynchro.common.rest;

public abstract class BaseRequest {
    private final String authorizationHeader;

    protected BaseRequest(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    protected abstract static class Builder {

        public abstract BaseRequest build();
    }
}
