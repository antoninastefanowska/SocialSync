package com.antonina.socialsynchro.services.backend;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.rest.BaseClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetRateLimitsRequest;
import com.antonina.socialsynchro.services.backend.requests.BackendGetTwitterVerifierRequest;
import com.antonina.socialsynchro.services.backend.requests.BackendUpdateRequestCounterRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetRateLimitsResponse;
import com.antonina.socialsynchro.services.backend.responses.BackendGetTwitterVerifierResponse;
import com.antonina.socialsynchro.services.backend.responses.BackendUpdateRequestCounterResponse;

import retrofit2.Call;

@SuppressWarnings("WeakerAccess")
public class BackendClient extends BaseClient {
    private final static String BASE_URL = "https://socialsynchro.pythonanywhere.com/backend/";

    private BackendClient() { }

    public static LiveData<BackendGetTwitterVerifierResponse> getTwitterVerifier(BackendGetTwitterVerifierRequest request) {
        GetTwitterVerifierController controller = new GetTwitterVerifierController(request);
        return controller.start();
    }

    public static LiveData<BackendGetRateLimitsResponse> getRateLimits(BackendGetRateLimitsRequest request) {
        GetRateLimitsController controller = new GetRateLimitsController(request);
        return controller.start();
    }

    public static LiveData<BackendUpdateRequestCounterResponse> updateRequestCounter(BackendUpdateRequestCounterRequest request) {
        UpdateRequestCounterController controller = new UpdateRequestCounterController(request);
        return controller.start();
    }

    private static class GetTwitterVerifierController extends BaseController<BackendGetTwitterVerifierRequest, BackendGetTwitterVerifierResponse> {

        public GetTwitterVerifierController(BackendGetTwitterVerifierRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected Call<BackendGetTwitterVerifierResponse> createCall() {
            BackendAPI backendAPI = retrofit.create(BackendAPI.class);
            return backendAPI.getTwitterVerifier(request.getLoginToken(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<BackendGetTwitterVerifierResponse> getResponseClass() {
            return BackendGetTwitterVerifierResponse.class;
        }
    }

    private static class GetRateLimitsController extends BaseController<BackendGetRateLimitsRequest, BackendGetRateLimitsResponse> {

        public GetRateLimitsController(BackendGetRateLimitsRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected Call<BackendGetRateLimitsResponse> createCall() {
            BackendAPI backendAPI = retrofit.create(BackendAPI.class);
            return backendAPI.getRateLimits(request.getEndpoint(), request.getServiceName());
        }

        @Override
        protected Class<BackendGetRateLimitsResponse> getResponseClass() {
            return BackendGetRateLimitsResponse.class;
        }
    }

    private static class UpdateRequestCounterController extends BaseController<BackendUpdateRequestCounterRequest, BackendUpdateRequestCounterResponse> {

        public UpdateRequestCounterController(BackendUpdateRequestCounterRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected Call<BackendUpdateRequestCounterResponse> createCall() {
            BackendAPI backendAPI = retrofit.create(BackendAPI.class);
            return backendAPI.updateRequestCounter(request.getEndpoint(), request.getServiceName(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<BackendUpdateRequestCounterResponse> getResponseClass() {
            return null;
        }
    }
}
