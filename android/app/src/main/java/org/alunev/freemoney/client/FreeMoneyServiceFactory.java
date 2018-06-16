package org.alunev.freemoney.client;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FreeMoneyServiceFactory {

    public FreeMoneyRestService createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:9000")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(FreeMoneyRestService.class);
    }
}
