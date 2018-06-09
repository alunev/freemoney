package org.alunev.freemoney.client;

import android.util.Log;

import org.alunev.freemoney.model.Sms;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SmsUploader {

    public static final String TAG = "SmsUploader";

    private final FreeMoneyService service;

    public SmsUploader() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:9000")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        service = retrofit.create(FreeMoneyService.class);
    }

    public void upload(Sms sms) {
        Log.i(TAG, "uploading: " + sms);

        service.processSms(sms).enqueue(new Callback<Sms>() {
            @Override
            public void onResponse(Call<Sms> call, Response<Sms> response) {
                Log.i(TAG, "Got response: " + response);
            }

            @Override
            public void onFailure(Call<Sms> call, Throwable t) {
                Log.e(TAG, "Error uploading: ", t);
            }
        });
    }
}
