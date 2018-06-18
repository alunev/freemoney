package org.alunev.freemoney.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestServiceFactory {
    private final Context context;

    public RestServiceFactory(Context context) {
        this.context = context;
    }

    public RestService createService() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.networkInterceptors().add(httpLoggingInterceptor);
        builder.addInterceptor(new ReceivedCookiesInterceptor(preferences));
        builder.addInterceptor(new AddCookiesInterceptor(preferences));
        builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:9000")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(builder.build())
                .build();

        return retrofit.create(RestService.class);
    }
}
