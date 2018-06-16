package org.alunev.freemoney.device;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import org.alunev.freemoney.model.Sms;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class SmsReader {
    private static final String TAG = "SmsReader";

    private final ContentResolver contentResolver;
    private final Context context;

    public SmsReader(Context context) {
        this.context = checkNotNull(context);
        this.contentResolver = context.getContentResolver();
    }

    public List<Sms> getSMS() {
        Cursor cur = contentResolver.query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);

        List<Sms> sms = new ArrayList<>();
        while (cur != null && cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex(Telephony.Sms.Inbox.ADDRESS));
            String body = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.Inbox.BODY));
            long dateSent = cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.Inbox.DATE));

            String deviceId = InstanceID.getInstance(context).getId();
            sms.add(Sms.createSms("", deviceId, address, body, dateSent));
        }

        if (cur != null) {
            cur.close();
        }

        Log.d(TAG, "have read " + sms.size() + " sms");

        return sms;
    }
}
