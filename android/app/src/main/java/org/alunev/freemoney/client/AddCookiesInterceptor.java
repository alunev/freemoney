package org.alunev.freemoney.client;

import android.content.SharedPreferences;
import android.util.Log;

import org.alunev.freemoney.utils.Preferences;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    private final SharedPreferences preferences;

    public AddCookiesInterceptor(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        Set<String> cookies = preferences.getStringSet(Preferences.COOKIES, new HashSet<>());
        for (String cookie : cookies) {
            builder.addHeader("Cookie", cookie);
            Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}