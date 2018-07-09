package ru.use.marathon.service;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import ru.use.marathon.Constants;
import ru.use.marathon.activities.MainActivity;
import ru.use.marathon.utils.NotificationUtils;

/**
 * Created by ilyas on 06-Jul-18.
 */

public class MessagingService extends FirebaseMessagingService {

    public static final String TAG = MessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage == null) return;

        if (remoteMessage.getData().size() > 0) {

            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                handleDataMessage(object,remoteMessage.getNotification().getBody());

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }


    private void handleDataMessage(JSONObject json,String message) throws JSONException {
        String name = json.getString("name");
        String timestamp = json.getString("timestamp");
        String chat_id = json.getString("chat_id");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message",message);
            pushNotification.putExtra("chat_id",chat_id);
            pushNotification.putExtra("name",name);
            pushNotification.putExtra("timestamp",timestamp);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }
    }

}
