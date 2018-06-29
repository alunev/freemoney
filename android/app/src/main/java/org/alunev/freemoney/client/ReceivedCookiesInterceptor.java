package org.alunev.freemoney.client;

import android.content.SharedPreferences;

import org.alunev.freemoney.utils.Preferences;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    private final SharedPreferences preferences;

    public ReceivedCookiesInterceptor(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>(originalResponse.headers("Set-Cookie"));

            preferences.edit()
                    .putStringSet(Preferences.COOKIES, cookies)
                    .apply();
        }

        return originalResponse;
    }
}