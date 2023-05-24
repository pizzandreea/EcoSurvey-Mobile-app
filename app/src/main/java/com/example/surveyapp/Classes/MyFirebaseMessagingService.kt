package com.example.surveyapp.Classes

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.surveyapp.MainActivity
import com.example.surveyapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId = "default_notification_channel_id"
const val channelName = "com.example.surveyapp"

//@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("NEW_TOKEN", token)

    }
    // generate the notification
    // attach the notification created with the costum layout
    // show the notification
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, description: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        //channel id, channel name
        //
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logotransparent)
            .setAutoCancel(true)
            .setContent(getRemoteView(title, description))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000)) // vibrates relaxes vibrates time in milliseconds
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, description))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, description: String): RemoteViews {
        val remoteView = RemoteViews("com.example.surveyapp", R.layout.notification)

        remoteView.setTextViewText(R.id.notificationTitle, title)
        remoteView.setTextViewText(R.id.notificationDescription, description)

        return remoteView

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage){
        Log.d("onMessageReceived", remoteMessage.toString())
        if(remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

}