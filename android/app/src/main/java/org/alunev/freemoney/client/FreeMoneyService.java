package org.alunev.freemoney.client;

import org.alunev.freemoney.model.Sms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FreeMoneyService {
    @POST("sms/process")
    Call<Sms> processSms(@Body Sms sms);
}