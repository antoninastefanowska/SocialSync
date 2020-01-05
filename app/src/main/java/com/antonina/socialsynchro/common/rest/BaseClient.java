package com.antonina.socialsynchro.common.rest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
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

        protected abstract ResponseType createResponse();

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
            //Log.d("deviantart", call.request().url().toString());
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
            //Log.d("blad", requestBodyToString(call.request().body()));
            if (response.isSuccessful())
                asyncResponse.setValue(response.body());
            else {
                try {
                    ResponseType objectResponse;
                    try {
                        objectResponse = gson.fromJson(response.errorBody().string(), getResponseClass());
                    } catch (IllegalStateException | JsonSyntaxException e) {
                        objectResponse = response.body();

                        if (objectResponse == null) {
                            objectResponse = createResponse();
                            objectResponse.setUndefinedError("Code: " + response.code() + " " + " Message: " + response.message());
                        }
                    }
                    if (objectResponse.getErrorString() == null)
                        objectResponse.setUndefinedError("Code: " + response.code() + " Message: " + response.message());
                    asyncResponse.setValue(objectResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseType> call, Throwable t) {
            ResponseType objectResponse = createResponse();
            objectResponse.setUndefinedError(t.getMessage());
            asyncResponse.setValue(objectResponse);
        }
    }

    protected static abstract class BaseRawController<RequestType extends BaseRequest, ResponseType extends IRawResponse> extends BaseControllerTemplate<RequestType, ResponseType> implements Callback<ResponseBody> {
        protected Retrofit retrofit;

        public BaseRawController(RequestType request) {
            super(request);
            retrofit = new Retrofit.Builder().baseUrl(getBaseURL()).addConverterFactory(ScalarsConverterFactory.create()).build();
        }

        protected abstract Call<ResponseBody> createCall();

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
            ResponseType objectResponse = createResponse();
            objectResponse.setUndefinedError(t.getMessage());
            asyncResponse.setValue(objectResponse);
        }
    }

    protected abstract static class BaseStringController<RequestType extends BaseRequest> implements Callback<ResponseBody> {
        protected final RequestType request;
        protected final MutableLiveData<String> asyncResponse;
        protected final Retrofit retrofit;

        public BaseStringController(RequestType request) {
            this.request = request;
            this.asyncResponse = new MutableLiveData<>();
            this.retrofit = new Retrofit.Builder().baseUrl(getBaseURL()).addConverterFactory(ScalarsConverterFactory.create()).build();
        }

        protected abstract String getBaseURL();

        protected abstract Call<ResponseBody> createCall();

        public LiveData<String> start() {
            Call<ResponseBody> call = createCall();
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Log.d("statystyki", requestBodyToString(call.request().body()));
            String stringResponse = "";
            if (response.isSuccessful()) {
                try {
                    stringResponse = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    stringResponse = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            asyncResponse.setValue(stringResponse);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            asyncResponse.setValue(t.getMessage());
        }
    }

    private static String requestBodyToString(RequestBody requestBody) {
        try {
            Buffer buffer = new Buffer();
            if (requestBody != null) {
                requestBody.writeTo(buffer);
                return buffer.readUtf8();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String headersToString(Headers headers) {
        StringBuilder sb = new StringBuilder();
        Map<String, List<String>> multimap = headers.toMultimap();
        for (Map.Entry<String, List<String>> entry : multimap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            for (String value : entry.getValue()) {
                sb.append(value);
                sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
