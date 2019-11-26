package com.antonina.socialsynchro.services.backend.requests;

public class BackendUpdateRequestCounterRequest extends BackendRequest {
    private final String endpoint;
    private final String serviceName;

    private BackendUpdateRequestCounterRequest(String authorizationString, String endpoint, String serviceName) {
        super(authorizationString);
        this.endpoint = endpoint;
        this.serviceName = serviceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BackendRequest.Builder {
        private String endpoint;
        private String serviceName;

        @Override
        public BackendUpdateRequestCounterRequest build() {
            configureAuthorization();
            return new BackendUpdateRequestCounterRequest(authorization.buildAuthorizationString(), endpoint, serviceName);
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }
    }
}
