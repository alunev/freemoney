package org.alunev.freemoney.client;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import org.alunev.freemoney.device.SmsReader;
import org.alunev.freemoney.model.Sms;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsSyncer {
    public static final String TAG = SmsSyncer.class.getCanonicalName();

    private final Context context;

    private SmsUploader smsUploader;

    public SmsSyncer(Context context) {
        this.context = context;
    }

    public boolean runSync() {
        RestService restService = new RestServiceFactory(context).createService();
        smsUploader = new SmsUploader(restService);

        Call<Long> call = restService.getLastSync(InstanceID.getInstance(context).getId());

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
        SmsReader smsReader = new SmsReader(context);
        List<Sms> smsList = smsReader.readAllSMS();
        Collections.sort(smsList, (sms1, sms2) -> (int) (sms2.getCreatedTs() - sms1.getCreatedTs()));

        int i = 0;
        while (i < smsList.size() && smsList.get(i).getCreatedTs() > timestamp) {
            smsUploader.upload(smsList.get(i++));
        }

        Log.i(TAG, "Uploaded " + i + " sms");
    }
}
