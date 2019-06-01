package com.antonina.socialsynchro.controllers;

import com.antonina.socialsynchro.accounts.IAccount;
import com.antonina.socialsynchro.posts.ChildPostContainer;
import com.antonina.socialsynchro.posts.IPost;
import com.antonina.socialsynchro.requests.OAuthToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookController implements IController, Callback<IPost> {
    private static final String BASE_URL = "https://graph.facebook.com/v2.8/";

    @Override
    public void requestToken(String credentials) {

    }

    @Override
    public void requestGet(String... args) {

    }

    @Override
    public void requestPost(ChildPostContainer post) {

    }

    @Override
    public void requestRemove(ChildPostContainer post) {

    }

    @Override
    public OAuthToken getToken() {
        return null;
    }

    @Override
    public void onResponse(Call<IPost> call, Response<IPost> response) {

    }

    @Override
    public void onFailure(Call<IPost> call, Throwable t) {

    }
}
