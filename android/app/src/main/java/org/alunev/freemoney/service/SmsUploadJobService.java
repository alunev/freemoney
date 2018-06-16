package org.alunev.freemoney.service;

import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.iid.InstanceID;

import org.alunev.freemoney.client.FreeMoneyRestService;
import org.alunev.freemoney.client.FreeMoneyServiceFactory;
import org.alunev.freemoney.client.SmsUploader;
import org.alunev.freemoney.device.SmsReader;
import org.alunev.freemoney.model.Sms;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsUploadJobService extends JobService {

    private static final String TAG = "SmsUploadJobService";

    private final FreeMoneyRestService restService;
    private final SmsUploader smsUploader;

    public SmsUploadJobService() {
        restService = new FreeMoneyServiceFactory().createService();
        smsUploader = new SmsUploader();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here

        Call<Long> call = restService.getLastSync(InstanceID.getInstance(getBaseContext()).getId());

        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    syncSince(response.body());
                } else {
                    Log.e(TAG, "Failed to getLastSync: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e(TAG, "Failed to getLastSync", t);
            }
        });

        return false; // Answers the question: "Is there still work going on?"
    }

    private void syncSince(Long timestamp) {
        SmsReader smsReader = new SmsReader(getApplicationContext());
        List<Sms> smsList = smsReader.getSMS();
        Collections.sort(smsList, (sms1, sms2) -> (int) (sms2.getCreatedTs() - sms1.getCreatedTs()));

        int i = 0;
        while (i < smsList.size() && smsList.get(i).getCreatedTs() > timestamp) {
            smsUploader.upload(smsList.get(i));
        }
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}