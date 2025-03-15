package com.example.bustracking;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.util.Log;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle received notification
        Log.d("FCM", "Message received: " + remoteMessage.getNotification().getBody());
    }
}
