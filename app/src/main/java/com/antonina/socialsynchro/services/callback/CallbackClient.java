package com.antonina.socialsynchro.services.callback;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.antonina.socialsynchro.services.IClient;
import com.antonina.socialsynchro.services.callback.requests.CallbackGetVerifierRequest;
import com.antonina.socialsynchro.services.callback.responses.CallbackGetVerifierResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("WeakerAccess")
public class CallbackClient implements IClient {
    private final static String BASE_URL = "https://socialsynchro.pythonanywhere.com/callback/";
    private static CallbackClient instance;

    public static CallbackClient getInstance() {
        if (instance == null)
            instance = new CallbackClient();
        return instance;
    }

    private CallbackClient() { }

    public LiveData<CallbackGetVerifierResponse> getVerifier(CallbackGetVerifierRequest request) {
        GetVerifierController controller = new GetVerifierController(request);
        return controller.start();
    }

    private static class GetVerifierController implements Callback<CallbackGetVerifierResponse> {
        private final CallbackGetVerifierRequest request;
        private final MutableLiveData<CallbackGetVerifierResponse> asyncResponse;

        public GetVerifierController(CallbackGetVerifierRequest request) {
            this.request = request;
            asyncResponse = new MutableLiveData<>();
        }

        public LiveData<CallbackGetVerifierResponse> start() {
            Gson gson = new Gson();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            CallbackAPI callbackAPI = retrofit.create(CallbackAPI.class);
            Call<CallbackGetVerifierResponse> call = callbackAPI.getVerifier(request.getLoginToken());
            call.enqueue(this);
            return asyncResponse;
        }

        @Override
        public void onResponse(Call<CallbackGetVerifierResponse> call, Response<CallbackGetVerifierResponse> response) {
            if (response.isSuccessful()) {
                asyncResponse.setValue(response.body());
            } else {
                try {
                    CallbackGetVerifierResponse objectResponse;
                    Gson gson = new Gson();
                    objectResponse = gson.fromJson(response.errorBody().string(), CallbackGetVerifierResponse.class);
                    asyncResponse.setValue(objectResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<CallbackGetVerifierResponse> call, Throwable t) {
            t.printStackTrace();
        }
    }
}
