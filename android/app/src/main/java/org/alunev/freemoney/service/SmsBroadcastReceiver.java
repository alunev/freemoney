package org.alunev.freemoney.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import org.alunev.freemoney.model.Sms;

import java.util.Objects;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private final String SENDER_TO_LISTEN = "Tinkoff";

    public SmsBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                if (!SENDER_TO_LISTEN.equals(smsMessage.getDisplayOriginatingAddress())) {
                    continue;
                }

                Sms.createSms("", "", smsMessage.getOriginatingAddress(), smsMessage.getMessageBody(), smsMessage.getTimestampMillis());
            }
        }
    }
}
