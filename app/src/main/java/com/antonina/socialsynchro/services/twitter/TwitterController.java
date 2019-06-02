package com.antonina.socialsynchro.services.twitter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.base.IAccount;
import com.antonina.socialsynchro.base.IController;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.services.twitter.requests.TwitterAccessTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterPostContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterLoginTokenRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterAccessTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterTweetResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterLoginTokenResponse;
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

public class TwitterController implements IController {
    private static final String BASE_URL = "https://api.twitter.com/";
    private static TwitterController instance;

    private TwitterLoginTokenResponse twitterLoginTokenResponse;
    private TwitterAccessTokenResponse twitterAccessTokenResponse;

    private final Context context = SocialSynchro.getAppContext();

    // TODO: uwzględnić limit 300 postów na 3 godziny

    private TwitterController() { }

    public static TwitterController getInstance() {
        if (instance == null)
            instance = new TwitterController();
        return instance;
    }

    @Override
    public void requestPost(ChildPostContainer post, IAccount account) {
        PostController postController = new PostController();
        postController.start(post, account);
    }

    @Override
    public void requestRemove(ChildPostContainer post, IAccount account) {
        RemoveController removeController = new RemoveController();
        removeController.start(post, account);
    }

    @Override
    public void requestGetLoginToken() {
        LoginTokenController loginTokenController = new LoginTokenController();
        loginTokenController.start();
    }

    @Override
    public void requestGetAccessToken(String loginToken, String secretLoginToken, String verifier) {
        AccessTokenController accessTokenController = new AccessTokenController();
        accessTokenController.start(loginToken, secretLoginToken, verifier);
    }

    public TwitterLoginTokenResponse getTwitterLoginTokenResponse() { return twitterLoginTokenResponse; }

    public TwitterAccessTokenResponse getTwitterAccessTokenResponse() { return twitterAccessTokenResponse; }

    private class LoginTokenController implements Callback<ResponseBody> {
        public void start() {
            TwitterLoginTokenRequest twitterLoginTokenRequest = new TwitterLoginTokenRequest();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<ResponseBody> call = twitterAPI.getLoginToken(twitterLoginTokenRequest.getAuthorization());
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                String responseString = "";
                try {
                    responseString = response.body().string();
                }
                catch (IOException e) { }

                twitterLoginTokenResponse = new TwitterLoginTokenResponse(responseString);

                if (twitterLoginTokenResponse.getCallbackConfirmed()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.twitter.com/oauth/authenticate?oauth_token=" + twitterLoginTokenResponse.getLoginToken()));
                    context.startActivity(browserIntent);
                }
            }
            else {
                String errors;
                try {
                    errors = response.errorBody().string();
                }
                catch (IOException e) {
                    errors = "";
                }
                Toast toast = Toast.makeText(context, "Wysłanie prośby o token skutkowało nieprawidłową odpowiedzią. " + errors, Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast toast = Toast.makeText(context, "Wysłanie prośby o token nie powiodło się.", Toast.LENGTH_SHORT);
            t.printStackTrace();
        }
    }

    private class AccessTokenController implements Callback<ResponseBody> {
        public void start(String requestToken, String secretToken, String verifier) {
            TwitterAccessTokenRequest twitterAccessTokenRequest = new TwitterAccessTokenRequest(requestToken, secretToken, verifier);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<ResponseBody> call = twitterAPI.getAccessToken(twitterAccessTokenRequest.getVerifier(), twitterAccessTokenRequest.getAuthorization());
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                String responseString = "";
                try {
                    responseString = response.body().string();
                }
                catch (IOException e) { }

                twitterAccessTokenResponse = new TwitterAccessTokenResponse(responseString);
                Toast toast = Toast.makeText(SocialSynchro.getAppContext(), "Logowanie pomyślne", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                TwitterTweetResponse twitterTweetResponse;
                try {
                    Gson gson = new Gson();
                    twitterTweetResponse = gson.fromJson(response.errorBody().string(), TwitterTweetResponse.class);
                }
                catch (IOException e) {
                    twitterTweetResponse = new TwitterTweetResponse();
                }
                Toast toast = Toast.makeText(context, "Logowanie skutkowało nieprawidłową odpowiedzią. " + response.code() + " " + twitterTweetResponse.getErrors(), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast toast = Toast.makeText(context, "Logowanie nie powiodło się.", Toast.LENGTH_SHORT);
            t.printStackTrace();
        }
    }

    private class PostController implements Callback<TwitterTweetResponse> {
        ChildPostContainer post;

        public void start(ChildPostContainer post, IAccount account) {
            this.post = post;
            TwitterPostContentRequest postRequest = new TwitterPostContentRequest(post, account);
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<TwitterTweetResponse> call = twitterAPI.postContent(postRequest.getStatus(), postRequest.getAuthorization());
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<TwitterTweetResponse> call, Response<TwitterTweetResponse> response) {
            if (response.isSuccessful()) {
                post.setServiceID(response.body().getID());
                Toast toast = Toast.makeText(context, "Wysłanie tweeta powiodło się.", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                TwitterTweetResponse twitterTweetResponse;
                try {
                    Gson gson = new Gson();
                    twitterTweetResponse = gson.fromJson(response.errorBody().string(), TwitterTweetResponse.class);
                }
                catch (IOException e) {
                    twitterTweetResponse = new TwitterTweetResponse();
                }
                Toast toast = Toast.makeText(context, "Wysłanie tweeta skutkowało nieprawidłową odpowiedzią. " + response.code() + " " + twitterTweetResponse.getErrors(), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onFailure(Call<TwitterTweetResponse> call, Throwable t) {
            Toast toast = Toast.makeText(context, "Wysłanie tweeta nie powiodło się.", Toast.LENGTH_SHORT);
            t.printStackTrace();
        }
    }

    private class RemoveController implements Callback<TwitterTweetResponse> {

        public void start(ChildPostContainer post, IAccount account) {
            TwitterRemoveContentRequest removeRequest = new TwitterRemoveContentRequest(post, account);
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);

            Call<TwitterTweetResponse> call = twitterAPI.removeContent(removeRequest.getID(), removeRequest.getAuthorization());
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<TwitterTweetResponse> call, Response<TwitterTweetResponse> response) {
            if (response.isSuccessful()) {
                Toast toast = Toast.makeText(context, "Usunięcie tweeta powiodło się.", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                TwitterTweetResponse twitterTweetResponse;
                try {
                    Gson gson = new Gson();
                    twitterTweetResponse = gson.fromJson(response.errorBody().string(), TwitterTweetResponse.class);
                }
                catch (IOException e) {
                    twitterTweetResponse = new TwitterTweetResponse();
                }
                Toast toast = Toast.makeText(context, "Wysłanie tweeta skutkowało nieprawidłową odpowiedzią. " + response.code() + " " + twitterTweetResponse.getErrors(), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onFailure(Call<TwitterTweetResponse> call, Throwable t) {
            Toast toast = Toast.makeText(context, "Usunięcie tweeta nie powiodło się.", Toast.LENGTH_SHORT);
            t.printStackTrace();
        }
    }
}
