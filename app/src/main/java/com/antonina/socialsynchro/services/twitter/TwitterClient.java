package com.antonina.socialsynchro.services.twitter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.antonina.socialsynchro.services.IClient;
import com.antonina.socialsynchro.services.IRawResponse;
import com.antonina.socialsynchro.services.twitter.requests.TwitterCreateContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterCreateContentWithMediaRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetAccessTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetBearerTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetLoginTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetUserRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterUploadAppendRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterUploadFinalizeRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterUploadInitRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterCheckUploadStatusRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterContentResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetAccessTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetBearerTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetLoginTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterUploadAppendResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterUploadFinalizeResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterUploadInitResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterCheckUploadStatusResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterUserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@SuppressWarnings({"WeakerAccess", "UnnecessaryCallToStringValueOf"})
public class TwitterClient implements IClient {
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

    private static abstract class BaseController<RequestClass extends TwitterRequest, ResponseClass extends TwitterResponse> implements Callback<ResponseClass> {
        protected final RequestClass request;
        private final MutableLiveData<ResponseClass> asyncResponse;

        public BaseController(RequestClass request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<>();
        }

        protected abstract Call<ResponseClass> createCall(TwitterAPI twitterAPI);

        protected abstract Class<ResponseClass> getResponseClass();

        protected abstract String getBaseURL();

        public LiveData<ResponseClass> start() {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(getBaseURL()).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<ResponseClass> call = createCall(twitterAPI);
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseClass> call, Response<ResponseClass> response) {
            if (response.isSuccessful())
                asyncResponse.setValue(response.body());
            else {
                try {
                    ResponseClass objectResponse;
                    Gson gson = new Gson();
                    objectResponse = gson.fromJson(response.errorBody().string(), getResponseClass());
                    if (objectResponse.getErrorString() == null)
                        objectResponse.setUndefinedError("Code: " + String.valueOf(response.code()));
                    asyncResponse.setValue(objectResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseClass> call, Throwable t) {
            t.printStackTrace();
        }
    }

    private static abstract class BaseRawController<RequestClass extends TwitterRequest, ResponseClass extends IRawResponse> implements Callback<ResponseBody> {
        protected final RequestClass request;
        private final MutableLiveData<ResponseClass> asyncResponse;

        public BaseRawController(RequestClass request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<>();
        }

        protected abstract Call<ResponseBody> createCall(TwitterAPI twitterAPI);

        protected abstract ResponseClass createResponse();

        public LiveData<ResponseClass> start() {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<ResponseBody> call = createCall(twitterAPI);
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            ResponseClass objectResponse = createResponse();
            if (response.isSuccessful()) {
                String stringResponse = "";
                try {
                    stringResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                objectResponse.createFromString(stringResponse);
            } else {
                String errorResponse = "";
                try {
                    errorResponse = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                objectResponse.createFromErrorString(errorResponse);
            }
            asyncResponse.setValue(objectResponse);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            t.printStackTrace();
        }
    }

    private static class GetLoginTokenController extends BaseRawController<TwitterGetLoginTokenRequest, TwitterGetLoginTokenResponse> {

        public GetLoginTokenController(TwitterGetLoginTokenRequest request) {
            super(request);
        }

        @Override
        protected Call<ResponseBody> createCall(TwitterAPI twitterAPI) {
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
        protected Call<ResponseBody> createCall(TwitterAPI twitterAPI) {
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
        protected Call<TwitterGetBearerTokenResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class VerifyCredentialsController extends BaseController<TwitterVerifyCredentialsRequest, TwitterUserResponse> {

        public VerifyCredentialsController(TwitterVerifyCredentialsRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUserResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class GetUserController extends BaseController<TwitterGetUserRequest, TwitterUserResponse> {

        public GetUserController(TwitterGetUserRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUserResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class CreateContentController extends BaseController<TwitterCreateContentRequest, TwitterContentResponse> {

        public CreateContentController(TwitterCreateContentRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class CreateContentWithMediaController extends BaseController<TwitterCreateContentWithMediaRequest, TwitterContentResponse> {

        public CreateContentWithMediaController(TwitterCreateContentWithMediaRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class RemoveContentController extends BaseController<TwitterRemoveContentRequest, TwitterContentResponse> {

        public RemoveContentController(TwitterRemoveContentRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class GetContentController extends BaseController<TwitterGetContentRequest, TwitterContentResponse> {

        public GetContentController(TwitterGetContentRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterContentResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class UploadInitController extends BaseController<TwitterUploadInitRequest, TwitterUploadInitResponse> {

        public UploadInitController(TwitterUploadInitRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUploadInitResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class UploadAppendController extends BaseController<TwitterUploadAppendRequest, TwitterUploadAppendResponse> {

        public UploadAppendController(TwitterUploadAppendRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUploadAppendResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class UploadFinalizeController extends BaseController<TwitterUploadFinalizeRequest, TwitterUploadFinalizeResponse> {

        public UploadFinalizeController(TwitterUploadFinalizeRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterUploadFinalizeResponse> createCall(TwitterAPI twitterAPI) {
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
    }

    private static class CheckUploadStatusController extends BaseController<TwitterCheckUploadStatusRequest, TwitterCheckUploadStatusResponse> {

        public CheckUploadStatusController(TwitterCheckUploadStatusRequest request) {
            super(request);
        }

        @Override
        protected Call<TwitterCheckUploadStatusResponse> createCall(TwitterAPI twitterAPI) {
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
    }
}