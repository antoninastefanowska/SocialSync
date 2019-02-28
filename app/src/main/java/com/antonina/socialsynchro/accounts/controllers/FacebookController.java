package com.antonina.socialsynchro.accounts.controllers;

import com.antonina.socialsynchro.accounts.IAccount;
import com.antonina.socialsynchro.posts.IPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookController implements IController, Callback<IPost> {
    private static final String BASE_URL = "https://graph.facebook.com/v2.8/";

    @Override
    public void startGet(String... args) {

    }

    @Override
    public void startPost(IPost post, IAccount account) {

    }

    @Override
    public void onResponse(Call<IPost> call, Response<IPost> response) {

    }

    @Override
    public void onFailure(Call<IPost> call, Throwable t) {

    }
}
