package com.antonina.socialsynchro.controllers;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.accounts.IAccount;
import com.antonina.socialsynchro.apis.TwitterAPI;
import com.antonina.socialsynchro.posts.ChildPostContainer;
import com.antonina.socialsynchro.posts.IPost;
import com.antonina.socialsynchro.requests.OAuthToken;
import com.antonina.socialsynchro.requests.TwitterPostRequest;
import com.antonina.socialsynchro.requests.TwitterRemoveRequest;
import com.antonina.socialsynchro.responses.TwitterPostResponse;
import com.antonina.socialsynchro.utils.APIKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Credentials;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterController implements IController {
    private static final String BASE_URL = "https://api.twitter.com/";
    private OAuthToken token;

    //private Context context, activity;
    private final Context context = SocialSynchro.getAppContext();

    // TODO: uwzględnić limit 300 postów na 3 godziny

    public TwitterController() {
        //context = SocialSynchro.getAppContext();
        //this.activity = activity;
    }

    @Override
    public void requestToken(String credentials) {
        TokenController tokenController = new TokenController();
        tokenController.start();
    }

    @Override
    public OAuthToken getToken() {
        return token;
    }

    @Override
    public void requestGet(String... args) {

    }

    @Override
    public void requestPost(ChildPostContainer post) {
        PostController postController = new PostController();
        postController.start(post);
    }

    @Override
    public void requestRemove(ChildPostContainer post) {
        RemoveController removeController = new RemoveController();
        removeController.start(post);
    }

    private class TokenController implements Callback<OAuthToken> {
        public void start() {
            String apiKey = Credentials.basic(APIKey.getKey(context, "twitter_key"), APIKey.getKey(context, "twitter_secretkey"));

            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            Call<OAuthToken> call = twitterAPI.getToken("client_credentials", apiKey);
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
            if (response.isSuccessful()) {
                token = response.body();
            }
            else {
                // TODO
            }
        }

        @Override
        public void onFailure(Call<OAuthToken> call, Throwable t) {
            // TODO
        }
    }

    private class PostController implements Callback<TwitterPostResponse> {
        ChildPostContainer post;

        public void start(ChildPostContainer post) {
            this.post = post;
            TwitterPostRequest postRequest = new TwitterPostRequest(post);
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);
            //Log.d("wysylanie", "Wysyłam tweeta: " + post.getContent());
            Call<TwitterPostResponse> call = twitterAPI.postContent(postRequest.getStatus(), postRequest.getAuthorization());
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<TwitterPostResponse> call, Response<TwitterPostResponse> response) {
            if (response.isSuccessful()) {
                //Log.d("wysylanie", "Wysłanie tweeta powiodło się. " + response.body().getId());
                post.setServiceID(response.body().getID());
                Toast toast = Toast.makeText(context, "Wysłanie tweeta powiodło się.", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                TwitterPostResponse twitterPostResponse;
                try {
                    Gson gson = new Gson();
                    twitterPostResponse = gson.fromJson(response.errorBody().string(), TwitterPostResponse.class);
                }
                catch (IOException e) {
                    twitterPostResponse = new TwitterPostResponse();
                }
                Toast toast = Toast.makeText(context, "Wysłanie tweeta skutkowało nieprawidłową odpowiedzią. " + response.code() + " " + twitterPostResponse.getErrors(), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onFailure(Call<TwitterPostResponse> call, Throwable t) {
            //Log.d("wysylanie", "Wysłanie tweeta nie powiodło się.");
            Toast toast = Toast.makeText(context, "Wysłanie tweeta nie powiodło się.", Toast.LENGTH_SHORT);
            t.printStackTrace();
        }
    }

    private class RemoveController implements Callback<TwitterPostResponse> {
        public void start(ChildPostContainer post) {
            TwitterRemoveRequest removeRequest = new TwitterRemoveRequest(post);
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            TwitterAPI twitterAPI = retrofit.create(TwitterAPI.class);

            Call<TwitterPostResponse> call = twitterAPI.removeContent(removeRequest.getID(), removeRequest.getAuthorization());
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<TwitterPostResponse> call, Response<TwitterPostResponse> response) {
            if (response.isSuccessful()) {
                Toast toast = Toast.makeText(context, "Usunięcie tweeta powiodło się.", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                TwitterPostResponse twitterPostResponse;
                try {
                    Gson gson = new Gson();
                    twitterPostResponse = gson.fromJson(response.errorBody().string(), TwitterPostResponse.class);
                }
                catch (IOException e) {
                    twitterPostResponse = new TwitterPostResponse();
                }
                Toast toast = Toast.makeText(context, "Wysłanie tweeta skutkowało nieprawidłową odpowiedzią. " + response.code() + " " + twitterPostResponse.getErrors(), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onFailure(Call<TwitterPostResponse> call, Throwable t) {
            Toast toast = Toast.makeText(context, "Usunięcie tweeta nie powiodło się.", Toast.LENGTH_SHORT);
            t.printStackTrace();
        }
    }
}
