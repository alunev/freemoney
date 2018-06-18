package org.alunev.freemoney.client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PlaySessionInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request newRequest = request.newBuilder()
                .addHeader("Cookie", "PLAY_SESSION=")
                .build();

        return chain.proceed(newRequest);
    }

}