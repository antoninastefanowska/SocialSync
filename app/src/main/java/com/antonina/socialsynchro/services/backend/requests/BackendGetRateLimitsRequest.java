package com.antonina.socialsynchro.services.backend.requests;

public class BackendGetRateLimitsRequest extends BackendRequest {
    private final String endpoint;
    private final String serviceName;

    private BackendGetRateLimitsRequest(String endpoint, String serviceName) {
        super(null);
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
        public BackendGetRateLimitsRequest build() {
            return new BackendGetRateLimitsRequest(endpoint, serviceName);
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
