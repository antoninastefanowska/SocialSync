package com.antonina.socialsynchro.services.backend;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.antonina.socialsynchro.common.rest.IClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetTwitterVerifierRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetTwitterVerifierResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("WeakerAccess")
public class BackendClient implements IClient {
    private final static String BASE_URL = "https://socialsynchro.pythonanywhere.com/callback/";
    private static BackendClient instance;

    public static BackendClient getInstance() {
        if (instance == null)
            instance = new BackendClient();
        return instance;
    }

    private BackendClient() { }

    public LiveData<BackendGetTwitterVerifierResponse> getVerifier(BackendGetTwitterVerifierRequest request) {
        GetTwitterVerifierController controller = new GetTwitterVerifierController(request);
        return controller.start();
    }

    private static class GetTwitterVerifierController implements Callback<BackendGetTwitterVerifierResponse> {
        private final BackendGetTwitterVerifierRequest request;
        private final MutableLiveData<BackendGetTwitterVerifierResponse> asyncResponse;

        public GetTwitterVerifierController(BackendGetTwitterVerifierRequest request) {
            this.request = request;
            asyncResponse = new MutableLiveData<>();
        }

        public LiveData<BackendGetTwitterVerifierResponse> start() {
            Gson gson = new Gson();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            BackendAPI backendAPI = retrofit.create(BackendAPI.class);
            Call<BackendGetTwitterVerifierResponse> call = backendAPI.getTwitterVerifier(request.getLoginToken());
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<BackendGetTwitterVerifierResponse> call, Response<BackendGetTwitterVerifierResponse> response) {
            if (response.isSuccessful()) {
                asyncResponse.setValue(response.body());
            } else {
                try {
                    BackendGetTwitterVerifierResponse objectResponse;
                    Gson gson = new Gson();
                    objectResponse = gson.fromJson(response.errorBody().string(), BackendGetTwitterVerifierResponse.class);
                    asyncResponse.setValue(objectResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<BackendGetTwitterVerifierResponse> call, Throwable t) {
            t.printStackTrace();
        }
    }
}
