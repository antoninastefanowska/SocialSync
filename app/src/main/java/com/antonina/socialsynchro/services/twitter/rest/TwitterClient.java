package com.antonina.socialsynchro.services.twitter.rest;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.rest.BaseClient;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterCreateContentRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterCreateContentWithMediaRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetAccessTokenRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetBearerTokenRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetContentRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetLoginTokenRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetRateLimitsRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetUserRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterUploadAppendRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterUploadFinalizeRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterUploadInitRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterCheckUploadStatusRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterContentResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetAccessTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetBearerTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetLoginTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetRateLimitsResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadAppendResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadFinalizeResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadInitResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterCheckUploadStatusResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;

@SuppressWarnings({"WeakerAccess", "UnnecessaryCallToStringValueOf"})
public class TwitterClient extends BaseClient {
    private static final String BASE_URL = "https://api.twitter.com/";
    private static final String BASE_UPLOAD_URL = "https://upload.twitter.com/";

    private TwitterClient() { }

    public static String getLoginURL(String loginToken) {
        return "https://api.twitter.com/oauth/authorize?oauth_token=" + loginToken + "&force_login=true";
    }

    public static LiveData<TwitterGetLoginTokenResponse> getLoginToken(TwitterGetLoginTokenRequest request) {
        GetLoginTokenController controller = new GetLoginTokenController(request);
        return controller.start();
    }

    public static LiveData<TwitterGetAccessTokenResponse> getAccessToken(TwitterGetAccessTokenRequest request) {
        GetAccessTokenController controller = new GetAccessTokenController(request);
        return controller.start();
    }

    public static LiveData<TwitterGetBearerTokenResponse> getBearerToken(TwitterGetBearerTokenRequest request) {
        GetBearerTokenController controller = new GetBearerTokenController(request);
        return controller.start();
    }

    public static LiveData<TwitterUserResponse> verifyCredentials(TwitterVerifyCredentialsRequest request) {
        VerifyCredentialsController controller = new VerifyCredentialsController(request);
        return controller.start();
    }

    public static LiveData<TwitterUserResponse> getUser(TwitterGetUserRequest request) {
        GetUserController controller = new GetUserController(request);
        return controller.start();
    }

    public static LiveData<TwitterContentResponse> createContent(TwitterCreateContentRequest request) {
        CreateContentController controller = new CreateContentController(request);
        return controller.start();
    }

    public static LiveData<TwitterContentResponse> createContentWithMedia(TwitterCreateContentWithMediaRequest request) {
        CreateContentWithMediaController controller = new CreateContentWithMediaController(request);
        return controller.start();
    }

    public static LiveData<TwitterContentResponse> removeContent(TwitterRemoveContentRequest request) {
        RemoveContentController controller = new RemoveContentController(request);
        return controller.start();
    }

    public static LiveData<TwitterContentResponse> getContent(TwitterGetContentRequest request) {
        GetContentController controller = new GetContentController(request);
        return controller.start();
    }

    public static LiveData<TwitterUploadInitResponse> uploadInit(TwitterUploadInitRequest request) {
        UploadInitController controller = new UploadInitController(request);
        return controller.start();
    }

    public static LiveData<TwitterUploadAppendResponse> uploadAppend(TwitterUploadAppendRequest request) {
        UploadAppendController controller = new UploadAppendController(request);
        return controller.start();
    }

    public static LiveData<TwitterUploadFinalizeResponse> uploadFinalize(TwitterUploadFinalizeRequest request) {
        UploadFinalizeController controller = new UploadFinalizeController(request);
        return controller.start();
    }

    public static LiveData<TwitterCheckUploadStatusResponse> checkUploadStatus(TwitterCheckUploadStatusRequest request) {
        CheckUploadStatusController controller = new CheckUploadStatusController(request);
        return controller.start();
    }

    public static LiveData<TwitterGetRateLimitsResponse> getRateLimits(TwitterGetRateLimitsRequest request) {
        GetRateLimitsController controller = new GetRateLimitsController(request);
        return controller.start();
    }

    private static class GetLoginTokenController extends BaseRawController<TwitterGetLoginTokenRequest, TwitterGetLoginTokenResponse> {

        public GetLoginTokenController(TwitterGetLoginTokenRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected Call<ResponseBody> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.getLoginToken(request.getAuthorizationHeader());
        }

        @Override
        protected TwitterGetLoginTokenResponse createResponse() {
            return new TwitterGetLoginTokenResponse();
        }
    }

    private static class GetAccessTokenController extends BaseRawController<TwitterGetAccessTokenRequest, TwitterGetAccessTokenResponse> {

        public GetAccessTokenController(TwitterGetAccessTokenRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected Call<ResponseBody> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.getAccessToken(request.getVerifier(), request.getAuthorizationHeader());
        }

        @Override
        protected TwitterGetAccessTokenResponse createResponse() {
            return new TwitterGetAccessTokenResponse();
        }
    }

    private static class GetBearerTokenController extends BaseController<TwitterGetBearerTokenRequest, TwitterGetBearerTokenResponse> {

        public GetBearerTokenController(TwitterGetBearerTokenRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterGetBearerTokenResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.getBearerToken(request.getGrantType(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterGetBearerTokenResponse> getResponseClass() {
            return TwitterGetBearerTokenResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterGetBearerTokenResponse createResponse() {
            return new TwitterGetBearerTokenResponse();
        }
    }

    private static class VerifyCredentialsController extends BaseController<TwitterVerifyCredentialsRequest, TwitterUserResponse> {

        public VerifyCredentialsController(TwitterVerifyCredentialsRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUserResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.verifyCredentials(request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterUserResponse> getResponseClass() {
            return TwitterUserResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterUserResponse createResponse() {
            return new TwitterUserResponse();
        }
    }

    private static class GetUserController extends BaseController<TwitterGetUserRequest, TwitterUserResponse> {

        public GetUserController(TwitterGetUserRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUserResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.getUser(request.getUserID(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterUserResponse> getResponseClass() {
            return TwitterUserResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterUserResponse createResponse() {
            return new TwitterUserResponse();
        }
    }

    private static class CreateContentController extends BaseController<TwitterCreateContentRequest, TwitterContentResponse> {

        public CreateContentController(TwitterCreateContentRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.createContent(request.getStatus(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterContentResponse> getResponseClass() {
            return TwitterContentResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterContentResponse createResponse() {
            return new TwitterContentResponse();
        }
    }

    private static class CreateContentWithMediaController extends BaseController<TwitterCreateContentWithMediaRequest, TwitterContentResponse> {

        public CreateContentWithMediaController(TwitterCreateContentWithMediaRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.createContentWithMedia(request.getStatus(), request.getMediaIDs(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterContentResponse> getResponseClass() {
            return TwitterContentResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterContentResponse createResponse() {
            return new TwitterContentResponse();
        }
    }

    private static class RemoveContentController extends BaseController<TwitterRemoveContentRequest, TwitterContentResponse> {

        public RemoveContentController(TwitterRemoveContentRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.removeContent(request.getID(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterContentResponse> getResponseClass() {
            return TwitterContentResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterContentResponse createResponse() {
            return new TwitterContentResponse();
        }
    }

    private static class GetContentController extends BaseController<TwitterGetContentRequest, TwitterContentResponse> {

        public GetContentController(TwitterGetContentRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.getContent(request.getID(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterContentResponse> getResponseClass() {
            return TwitterContentResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterContentResponse createResponse() {
            return new TwitterContentResponse();
        }
    }

    private static class UploadInitController extends BaseController<TwitterUploadInitRequest, TwitterUploadInitResponse> {

        public UploadInitController(TwitterUploadInitRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUploadInitResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.uploadInit(request.getCommand(), request.getTotalBytes(), request.getMediaType(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterUploadInitResponse> getResponseClass() {
            return TwitterUploadInitResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_UPLOAD_URL;
        }

        @Override
        protected TwitterUploadInitResponse createResponse() {
            return new TwitterUploadInitResponse();
        }
    }

    private static class UploadAppendController extends BaseController<TwitterUploadAppendRequest, TwitterUploadAppendResponse> {

        public UploadAppendController(TwitterUploadAppendRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUploadAppendResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.uploadAppend(request.getCommand(), request.getMediaID(), request.getSegmentIndex(), request.getMedia(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterUploadAppendResponse> getResponseClass() {
            return TwitterUploadAppendResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_UPLOAD_URL;
        }

        @Override
        protected TwitterUploadAppendResponse createResponse() {
            return new TwitterUploadAppendResponse();
        }
    }

    private static class UploadFinalizeController extends BaseController<TwitterUploadFinalizeRequest, TwitterUploadFinalizeResponse> {

        public UploadFinalizeController(TwitterUploadFinalizeRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUploadFinalizeResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.uploadFinalize(request.getCommand(), request.getMediaID(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterUploadFinalizeResponse> getResponseClass() {
            return TwitterUploadFinalizeResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_UPLOAD_URL;
        }

        @Override
        protected TwitterUploadFinalizeResponse createResponse() {
            return new TwitterUploadFinalizeResponse();
        }
    }

    private static class CheckUploadStatusController extends BaseController<TwitterCheckUploadStatusRequest, TwitterCheckUploadStatusResponse> {

        public CheckUploadStatusController(TwitterCheckUploadStatusRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterCheckUploadStatusResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.checkUploadStatus(request.getCommand(), request.getMediaID(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterCheckUploadStatusResponse> getResponseClass() {
            return TwitterCheckUploadStatusResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_UPLOAD_URL;
        }

        @Override
        protected TwitterCheckUploadStatusResponse createResponse() {
            return new TwitterCheckUploadStatusResponse();
        }
    }

    private static class GetRateLimitsController extends BaseController<TwitterGetRateLimitsRequest, TwitterGetRateLimitsResponse> {

        public GetRateLimitsController(TwitterGetRateLimitsRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterGetRateLimitsResponse> createCall() {
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            return twitterAPI.getRateLimits(request.getResources(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<TwitterGetRateLimitsResponse> getResponseClass() {
            return TwitterGetRateLimitsResponse.class;
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected TwitterGetRateLimitsResponse createResponse() {
            return new TwitterGetRateLimitsResponse();
        }
    }
}