package org.alunev.freemoney.service;

import android.app.IntentService;
import android.content.Intent;

public class SmsUploadService extends IntentService {

    public SmsUploadService() {
        super(SmsUploadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

    }
}