package org.alunev.freemoney.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.alunev.freemoney.client.SmsUploader;
import org.alunev.freemoney.model.Sms;

import java.util.Objects;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private final static String SENDER_TO_LISTEN = "Tinkoff";

    private final SmsUploader smsUploader;

    public SmsBroadcastReceiver() {
        smsUploader = new SmsUploader();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Got sms", Toast.LENGTH_LONG).show();

        if (Objects.equals(intent.getAction(), Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                Log.i("SmsBroadcastReceiver", "Received: " + smsMessage);

                if (!SENDER_TO_LISTEN.equals(smsMessage.getDisplayOriginatingAddress())) {
                    continue;
                }

                Sms sms = Sms.createSms("5b1315ca6d0a223a2ba749db", "", smsMessage.getOriginatingAddress(), smsMessage.getMessageBody(), smsMessage.getTimestampMillis());

                smsUploader.upload(sms);
            }
        }
    }
}
