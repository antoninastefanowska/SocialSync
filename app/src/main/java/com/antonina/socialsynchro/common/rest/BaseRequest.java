package com.antonina.socialsynchro.common.rest;

public abstract class BaseRequest {
    private final String authorizationString;

    protected BaseRequest(String authorizationString) {
        this.authorizationString = authorizationString;
    }

    public String getAuthorizationString() {
        return authorizationString;
    }

    protected abstract static class Builder {

        public abstract BaseRequest build();
    }
}
