package org.alunev.freemoney.client;

import android.util.Log;

import org.alunev.freemoney.model.Sms;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsUploader {

    public static final String TAG = "SmsUploader";

    private final RestService service;

    public SmsUploader(RestService service) {
        this.service = service;
    }

    public void upload(Sms sms) {
        Log.i(TAG, "uploading: " + sms);

        service.processSms(sms).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "Got response: " + response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error uploading: ", t);
            }
        });
    }
}
