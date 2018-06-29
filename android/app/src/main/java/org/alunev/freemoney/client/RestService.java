package org.alunev.freemoney.client;

import org.alunev.freemoney.model.Sms;
import org.alunev.freemoney.model.SmsBulk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {
    @POST("api/sms/process")
    Call<Void> processSms(@Body Sms sms);

    @POST("api/sms/process/bulk")
    Call<Void> processSmsBulk(@Body SmsBulk smsBulk);

    @GET("api/user/{instanceId}/lastSync")
    Call<Long> getLastSync(@Path("instanceId") String instanceId);

    @POST("api/tokensignin")
    Call<String> tokenSignIn(@Body String tokenId);
}