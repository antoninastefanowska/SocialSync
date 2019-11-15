package com.antonina.socialsynchro.services.backend.requests;

import com.antonina.socialsynchro.common.rest.BaseRequest;
import com.antonina.socialsynchro.services.backend.authorization.BackendAuthorizationStrategy;

public abstract class BackendRequest extends BaseRequest {

    protected BackendRequest(String authorizationHeader) {
        super(authorizationHeader);
    }

    protected static abstract class Builder extends BaseRequest.Builder {
        protected BackendAuthorizationStrategy authorization;

        @Override
        public abstract BackendRequest build();

        protected void configureAuthorization() {
            authorization = new BackendAuthorizationStrategy();
        }
    }
}
