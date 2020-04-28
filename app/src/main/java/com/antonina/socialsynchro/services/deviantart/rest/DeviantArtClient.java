package com.antonina.socialsynchro.services.deviantart.rest;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.rest.BaseClient;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetAccessTokenRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetCategoryTreeRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetDeviationRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetGalleriesRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetUserdataRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtRefreshTokenRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtStashDeleteRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtStashPublishRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtStashSubmitRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtWhoAmIRequest;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtAccessTokenResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtDeviationResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetCategoryTreeResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetGalleriesResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetUserdataResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtResultResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtStashPublishResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtStashSubmitResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtUserResponse;

import retrofit2.Call;

public class DeviantArtClient extends BaseClient {
    private static final String BASE_URL = "https://www.deviantart.com/";
    private static final String CALLBACK_URL = "https://socialsynchro.pythonanywhere.com/backend/post_deviantart_code";

    private DeviantArtClient() { }

    public static String getLoginURL(String state) {
        ApplicationConfig config = ApplicationConfig.getInstance();
        String clientID = config.getKey("deviantart_client_id");
        String scope = "stash publish browse user";
        return "https://www.deviantart.com/oauth2/authorize?response_type=code&view=login&scope=" + scope + "&client_id=" + clientID + "&redirect_uri=" + CALLBACK_URL + "&state=" + state;
    }

    public static String getCallbackURL() {
        return CALLBACK_URL;
    }

    public static LiveData<DeviantArtAccessTokenResponse> getAccessToken(DeviantArtGetAccessTokenRequest request) {
        GetAccessTokenController controller = new GetAccessTokenController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtAccessTokenResponse> refreshToken(DeviantArtRefreshTokenRequest request) {
        RefreshTokenController controller = new RefreshTokenController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtUserResponse> whoAmI(DeviantArtWhoAmIRequest request) {
        WhoAmIController controller = new WhoAmIController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtStashSubmitResponse> stashSubmit(DeviantArtStashSubmitRequest request) {
        StashSubmitController controller = new StashSubmitController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtStashPublishResponse> stashPublish(DeviantArtStashPublishRequest request) {
        StashPublishController controller = new StashPublishController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtResultResponse> stashDelete(DeviantArtStashDeleteRequest request) {
        StashDeleteController controller = new StashDeleteController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtDeviationResponse> getDeviation(DeviantArtGetDeviationRequest request) {
        GetDeviationController controller = new GetDeviationController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtGetGalleriesResponse> getGalleries(DeviantArtGetGalleriesRequest request) {
        GetGalleriesController controller = new GetGalleriesController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtGetUserdataResponse> getUserdata(DeviantArtGetUserdataRequest request) {
        GetUserdataController controller = new GetUserdataController(request);
        return controller.start();
    }

    public static LiveData<DeviantArtGetCategoryTreeResponse> getCategoryTree(DeviantArtGetCategoryTreeRequest request) {
        GetCategoryTreeController controller = new GetCategoryTreeController(request);
        return controller.start();
    }

    private static class GetAccessTokenController extends BaseController<DeviantArtGetAccessTokenRequest, DeviantArtAccessTokenResponse> {

        public GetAccessTokenController(DeviantArtGetAccessTokenRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtAccessTokenResponse createResponse() {
            return new DeviantArtAccessTokenResponse();
        }

        @Override
        protected Call<DeviantArtAccessTokenResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.getAccessToken(request.getClientID(), request.getClientSecret(), request.getGrantType(), request.getCode(), request.getRedirectURL());
        }

        @Override
        protected Class<DeviantArtAccessTokenResponse> getResponseClass() {
            return DeviantArtAccessTokenResponse.class;
        }
    }

    private static class RefreshTokenController extends BaseController<DeviantArtRefreshTokenRequest, DeviantArtAccessTokenResponse> {

        public RefreshTokenController(DeviantArtRefreshTokenRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtAccessTokenResponse createResponse() {
            return new DeviantArtAccessTokenResponse();
        }

        @Override
        protected Call<DeviantArtAccessTokenResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.refreshToken(request.getClientID(), request.getClientSecret(), request.getGrantType(), request.getRefreshToken());
        }

        @Override
        protected Class<DeviantArtAccessTokenResponse> getResponseClass() {
            return DeviantArtAccessTokenResponse.class;
        }
    }

    private static class WhoAmIController extends BaseController<DeviantArtWhoAmIRequest, DeviantArtUserResponse> {

        public WhoAmIController(DeviantArtWhoAmIRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtUserResponse createResponse() {
            return new DeviantArtUserResponse();
        }

        @Override
        protected Call<DeviantArtUserResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.whoAmI(request.getExpandOptions(), request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtUserResponse> getResponseClass() {
            return DeviantArtUserResponse.class;
        }
    }

    private static class GetGalleriesController extends BaseController<DeviantArtGetGalleriesRequest, DeviantArtGetGalleriesResponse> {

        public GetGalleriesController(DeviantArtGetGalleriesRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtGetGalleriesResponse createResponse() {
            return new DeviantArtGetGalleriesResponse();
        }

        @Override
        protected Call<DeviantArtGetGalleriesResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.getGalleries(request.getLimit(), request.getOffset(), request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtGetGalleriesResponse> getResponseClass() {
            return DeviantArtGetGalleriesResponse.class;
        }
    }

    private static class GetUserdataController extends BaseController<DeviantArtGetUserdataRequest, DeviantArtGetUserdataResponse> {

        public GetUserdataController(DeviantArtGetUserdataRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtGetUserdataResponse createResponse() {
            return new DeviantArtGetUserdataResponse();
        }

        @Override
        protected Call<DeviantArtGetUserdataResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.getUserdata(request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtGetUserdataResponse> getResponseClass() {
            return DeviantArtGetUserdataResponse.class;
        }
    }

    private static class GetCategoryTreeController extends BaseController<DeviantArtGetCategoryTreeRequest, DeviantArtGetCategoryTreeResponse> {

        public GetCategoryTreeController(DeviantArtGetCategoryTreeRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtGetCategoryTreeResponse createResponse() {
            return new DeviantArtGetCategoryTreeResponse();
        }

        @Override
        protected Call<DeviantArtGetCategoryTreeResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.getCategoryTree(request.getCatpath(), request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtGetCategoryTreeResponse> getResponseClass() {
            return DeviantArtGetCategoryTreeResponse.class;
        }
    }

    private static class StashSubmitController extends BaseController<DeviantArtStashSubmitRequest, DeviantArtStashSubmitResponse> {

        public StashSubmitController(DeviantArtStashSubmitRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtStashSubmitResponse createResponse() {
            return new DeviantArtStashSubmitResponse();
        }

        @Override
        protected Call<DeviantArtStashSubmitResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.stashSubmit(request.getFile(), request.getTitle(), request.getDescription(), request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtStashSubmitResponse> getResponseClass() {
            return DeviantArtStashSubmitResponse.class;
        }
    }

    private static class StashPublishController extends BaseController<DeviantArtStashPublishRequest, DeviantArtStashPublishResponse> {

        public StashPublishController(DeviantArtStashPublishRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtStashPublishResponse createResponse() {
            return new DeviantArtStashPublishResponse();
        }

        @Override
        protected Call<DeviantArtStashPublishResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.stashPublish(request.getStashID(), request.isMature(), request.agreeSubmission(), request.agreeTOS(), request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtStashPublishResponse> getResponseClass() {
            return DeviantArtStashPublishResponse.class;
        }
    }

    private static class StashDeleteController extends BaseController<DeviantArtStashDeleteRequest, DeviantArtResultResponse> {

        public StashDeleteController(DeviantArtStashDeleteRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtResultResponse createResponse() {
            return new DeviantArtResultResponse();
        }

        @Override
        protected Call<DeviantArtResultResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.stashDelete(request.getStashID(), request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtResultResponse> getResponseClass() {
            return DeviantArtResultResponse.class;
        }
    }

    private static class GetDeviationController extends BaseController<DeviantArtGetDeviationRequest, DeviantArtDeviationResponse> {

        public GetDeviationController(DeviantArtGetDeviationRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected DeviantArtDeviationResponse createResponse() {
            return new DeviantArtDeviationResponse();
        }

        @Override
        protected Call<DeviantArtDeviationResponse> createCall() {
            DeviantArtAPI deviantArtAPI = retrofit.create(DeviantArtAPI.class);
            return deviantArtAPI.getDeviation(request.getDeviationID(), request.getAuthorizationString());
        }

        @Override
        protected Class<DeviantArtDeviationResponse> getResponseClass() {
            return DeviantArtDeviationResponse.class;
        }
    }
}
