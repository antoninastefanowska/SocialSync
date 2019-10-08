package com.antonina.socialsynchro.common.rest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

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

public abstract class BaseClient {

    private static abstract class BaseControllerTemplate<RequestType extends BaseRequest, ResponseType extends IResponse> {
        protected final RequestType request;
        protected final MutableLiveData<ResponseType> asyncResponse;

        public BaseControllerTemplate(RequestType request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<>();
        }

        protected abstract String getBaseURL();

        public abstract LiveData<ResponseType> start();
    }

    protected static abstract class BaseController<RequestType extends BaseRequest, ResponseType extends IResponse> extends BaseControllerTemplate<RequestType, ResponseType> implements Callback<ResponseType> {
        private Gson gson;
        protected Retrofit retrofit;

        public BaseController(RequestType request) {
            super(request);
            gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder().baseUrl(getBaseURL()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }

        protected abstract Call<ResponseType> createCall();

        protected abstract Class<ResponseType> getResponseClass();

        public LiveData<ResponseType> start() {
            Call<ResponseType> call = createCall();
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
            if (response.isSuccessful())
                asyncResponse.setValue(response.body());
            else {
                try {
                    ResponseType objectResponse;
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
        public void onFailure(Call<ResponseType> call, Throwable t) {
            t.printStackTrace();
        }
    }

    protected static abstract class BaseRawController<RequestType extends BaseRequest, ResponseType extends IRawResponse> extends BaseControllerTemplate<RequestType, ResponseType> implements Callback<ResponseBody> {
        protected Retrofit retrofit;

        public BaseRawController(RequestType request) {
            super(request);
            retrofit = new Retrofit.Builder().baseUrl(getBaseURL()).addConverterFactory(ScalarsConverterFactory.create()).build();
        }

        protected abstract Call<ResponseBody> createCall();

        protected abstract ResponseType createResponse();

        public LiveData<ResponseType> start() {
            Call<ResponseBody> call = createCall();
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            ResponseType objectResponse = createResponse();
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
}
