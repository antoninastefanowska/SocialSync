package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.common.rest.BaseRequest;
import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookAuthorizationStrategy;

public abstract class FacebookRequest extends BaseRequest {

    protected FacebookRequest(String authorizationHeader) {
        super(authorizationHeader);
    }

    public static abstract class Builder extends BaseRequest.Builder {
        protected FacebookAuthorizationStrategy authorization;

        @Override
        public abstract FacebookRequest build();
    }
}
