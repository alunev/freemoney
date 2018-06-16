package org.alunev.freemoney.client;

import org.alunev.freemoney.model.Sms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FreeMoneyRestService {
    @POST("sms/process")
    Call<Sms> processSms(@Body Sms sms);

    @GET("user/{instanceId}/lastSync")
    Call<Long> getLastSync(@Path("instanceId") String instanceId);
}