package org.alunev.freemoney.service;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.iid.InstanceID;

import org.alunev.freemoney.client.RestService;
import org.alunev.freemoney.client.RestServiceFactory;
import org.alunev.freemoney.client.SmsSyncer;
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

    private SmsUploader smsUploader;

    public SmsUploadJobService() {

    }

    @Override
    public boolean onStartJob(JobParameters job) {
        return new SmsSyncer(getApplicationContext()).runSync(); // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}