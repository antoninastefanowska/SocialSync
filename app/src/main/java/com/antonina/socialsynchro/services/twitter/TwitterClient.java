package com.antonina.socialsynchro.services.twitter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.antonina.socialsynchro.services.twitter.requests.TwitterCreateContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetAccessTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetLoginTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterContentResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetAccessTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetLoginTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterVerifyCredentialsResponse;
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

public class TwitterClient {
    private static final String BASE_URL = "https://api.twitter.com/";
    private static TwitterClient instance;

    public static TwitterClient getInstance() {
        if (instance == null)
            instance = new TwitterClient();
        return instance;
    }

    private TwitterClient() { }

    public static String getLoginUrl(String loginToken) {
        return "https://api.twitter.com/oauth/authorize?oauth_token=" + loginToken + "&force_login=true";
    }

    public LiveData<TwitterGetLoginTokenResponse> getLoginToken(TwitterGetLoginTokenRequest request) {
        GetLoginTokenController controller = new GetLoginTokenController(request);
        return controller.start();
    }

    public LiveData<TwitterGetAccessTokenResponse> getAccessToken(TwitterGetAccessTokenRequest request) {
        GetAccessTokenController controller = new GetAccessTokenController(request);
        return controller.start();
    }

    public LiveData<TwitterContentResponse> createContent(TwitterCreateContentRequest request) {
        CreateContentController controller = new CreateContentController(request);
        return controller.start();
    }

    public LiveData<TwitterContentResponse> removeContent(TwitterRemoveContentRequest request) {
        RemoveContentController controller = new RemoveContentController(request);
        return controller.start();
    }

    public LiveData<TwitterVerifyCredentialsResponse> verifyCredentials(TwitterVerifyCredentialsRequest request) {
        VerifyCredentialsController controller = new VerifyCredentialsController(request);
        return controller.start();
    }

    private static class GetLoginTokenController implements Callback<ResponseBody> {
        private TwitterGetLoginTokenRequest request;
        private MutableLiveData<TwitterGetLoginTokenResponse> asyncResponse;

        public GetLoginTokenController(TwitterGetLoginTokenRequest request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<TwitterGetLoginTokenResponse>();
        }

        public LiveData<TwitterGetLoginTokenResponse> start() {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<ResponseBody> call = twitterAPI.getLoginToken(request.getAuthorizationHeader());
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            TwitterGetLoginTokenResponse objectResponse = new TwitterGetLoginTokenResponse();
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

    private static class GetAccessTokenController implements Callback<ResponseBody> {
        private TwitterGetAccessTokenRequest request;
        private MutableLiveData<TwitterGetAccessTokenResponse> asyncResponse;

        public GetAccessTokenController(TwitterGetAccessTokenRequest request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<TwitterGetAccessTokenResponse>();
        }

        public LiveData<TwitterGetAccessTokenResponse> start() {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<ResponseBody> call = twitterAPI.getAccessToken(request.getVerifier(), request.getAuthorizationHeader());
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            TwitterGetAccessTokenResponse objectResponse = new TwitterGetAccessTokenResponse();
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

    private static class CreateContentController implements Callback<TwitterContentResponse> {
        private TwitterCreateContentRequest request;
        private MutableLiveData<TwitterContentResponse> asyncResponse;

        public CreateContentController(TwitterCreateContentRequest request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<TwitterContentResponse>();
        }

        public LiveData<TwitterContentResponse> start() {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<TwitterContentResponse> call = twitterAPI.createContent(request.getStatus(), request.getAuthorizationHeader());
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<TwitterContentResponse> call, Response<TwitterContentResponse> response) {
            if (response.isSuccessful()) {
                asyncResponse.setValue(response.body());
            } else {
                try {
                    TwitterContentResponse objectResponse;
                    Gson gson = new Gson();
                    objectResponse = gson.fromJson(response.errorBody().string(), TwitterContentResponse.class);
                    asyncResponse.setValue(objectResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<TwitterContentResponse> call, Throwable t) {
            t.printStackTrace();
        }
    }

    private static class RemoveContentController implements Callback<TwitterContentResponse> {
        private TwitterRemoveContentRequest request;
        private MutableLiveData<TwitterContentResponse> asyncResponse;

        public RemoveContentController(TwitterRemoveContentRequest request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<TwitterContentResponse>();
        }

        public MutableLiveData<TwitterContentResponse> start() {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<TwitterContentResponse> call = twitterAPI.removeContent(request.getID(), request.getAuthorizationHeader());
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<TwitterContentResponse> call, Response<TwitterContentResponse> response) {
            if (response.isSuccessful()) {
                asyncResponse.setValue(response.body());
            } else {
                try {
                    TwitterContentResponse objectResponse;
                    Gson gson = new Gson();
                    objectResponse = gson.fromJson(response.errorBody().string(), TwitterContentResponse.class);
                    asyncResponse.setValue(objectResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<TwitterContentResponse> call, Throwable t) {
            t.printStackTrace();
        }
    }

    private static class VerifyCredentialsController implements Callback<TwitterVerifyCredentialsResponse> {
        private TwitterVerifyCredentialsRequest request;
        private MutableLiveData<TwitterVerifyCredentialsResponse> asyncResponse;

        public VerifyCredentialsController(TwitterVerifyCredentialsRequest request) {
            this.request = request;
            asyncResponse = new MutableLiveData<TwitterVerifyCredentialsResponse>();
        }

        public LiveData<TwitterVerifyCredentialsResponse> start() {
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<TwitterVerifyCredentialsResponse> call = twitterAPI.verifyCredentials(request.getAuthorizationHeader());
            call.enqueue(this);
            Log.d("konto", "Request content: " + call.request().toString());
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<TwitterVerifyCredentialsResponse> call, Response<TwitterVerifyCredentialsResponse> response) {
            if (response.isSuccessful()) {
                asyncResponse.setValue(response.body());
            } else {
                try {
                    TwitterVerifyCredentialsResponse objectResponse;
                    Gson gson = new Gson();
                    objectResponse = gson.fromJson(response.errorBody().string(), TwitterVerifyCredentialsResponse.class);
                    asyncResponse.setValue(objectResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<TwitterVerifyCredentialsResponse> call, Throwable t) {
            t.printStackTrace();
        }
    }
}
