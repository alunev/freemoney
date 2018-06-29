package org.alunev.freemoney.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.google.common.base.Strings;

import org.alunev.freemoney.device.SmsReader;
import org.alunev.freemoney.model.Sms;
import org.alunev.freemoney.model.SmsBulk;
import org.alunev.freemoney.utils.Preferences;
import org.alunev.freemoney.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsSyncer {
    public static final String TAG = SmsSyncer.class.getCanonicalName();

    private final Context context;
    private final RestService restService;

    private SmsUploader smsUploader;

    public SmsSyncer(Context context) {
        this.context = context;
        restService = new RestServiceFactory(context).createService();
    }

    public boolean runSync() {
        String userId = Utils.getUserId(context);
        if (Strings.isNullOrEmpty(userId)) {
            Log.i(TAG, "Skip sms read: No current user auth id found.");
            return true;
        }

        smsUploader = new SmsUploader(restService);

        Call<Long> call = restService.getLastSync(InstanceID.getInstance(context).getId());

        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    syncSince(response.body(), userId);
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

    private void syncSince(Long timestamp, String userId) {
        SmsReader smsReader = new SmsReader(context);
        long now = new Date().getTime();

        List<Sms> smsList = new ArrayList<>();
        List<Sms> allSMS = smsReader.readAllSMS();
        for (Sms sms : allSMS) {
            if (sms.getCreatedTs() - timestamp > 0) {
                smsList.add(sms);
            }
        }

        if (smsList.isEmpty()) {
            Log.i(TAG, "No new sms to sync");
            return;
        }

        SmsBulk bulk = new SmsBulk(userId, InstanceID.getInstance(context).getId(), now, smsList);

        restService.processSmsBulk(bulk).enqueue(
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.i(TAG, "Uploaded " + smsList.size() + " sms");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Failed to upload", t);
                    }
                }
        );

    }
}
