package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.common.rest.BaseRequest;
import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

public abstract class DeviantArtRequest extends BaseRequest {

    protected DeviantArtRequest(String authorizationString) {
        super(authorizationString);
    }

    public static abstract class Builder extends BaseRequest.Builder {
        protected DeviantArtAuthorizationStrategy authorization;

        @Override
        public abstract DeviantArtRequest build();
    }
}
