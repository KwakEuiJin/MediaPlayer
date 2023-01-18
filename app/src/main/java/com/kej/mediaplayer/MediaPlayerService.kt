package com.kej.mediaplayer

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import com.kej.mediaplayer.MainActivity.Companion.MEDIA_PAUSE
import com.kej.mediaplayer.MainActivity.Companion.MEDIA_PLAY
import com.kej.mediaplayer.MainActivity.Companion.MEDIA_STOP

class MediaPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val channelId = "MEDIA_PLAYER_CHANNEL"
    private val receiver = LowBatteryBroadCastReceiver()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initReceiver()
        startForeground(100, createMediaNotification())
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MEDIA_PLAY -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(baseContext, R.raw.start_gaho)
                }
                mediaPlayer?.start()
            }

            MEDIA_PAUSE -> {
                mediaPlayer?.pause()
            }

            MEDIA_STOP -> {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                stopSelf()
            }

        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("MEDIA_PLAYER_CHANNEL", "MEDIA_PLAYER", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = baseContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(mediaAction: String): PendingIntent {
        return PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = mediaAction
            },
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createMediaNotification(): Notification {
        val mediaNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playIcon:Icon = Icon.createWithResource(baseContext, R.drawable.ic_baseline_play_arrow_24)
            val pauseIcon = Icon.createWithResource(baseContext, R.drawable.ic_baseline_pause_24)
            val stopIcon = Icon.createWithResource(baseContext, R.drawable.ic_baseline_stop_24)
            Notification.Builder(baseContext, channelId)
                .setStyle(
                    Notification.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .addAction(
                    Notification.Action.Builder(
                        pauseIcon, "pause", createPendingIntent(MEDIA_PAUSE)
                    ).build()
                )
                .addAction(
                    Notification.Action.Builder(
                        playIcon, "play", createPendingIntent(MEDIA_PLAY)
                    ).build()
                )
                .addAction(
                    Notification.Action.Builder(
                        stopIcon, "stop", createPendingIntent(MEDIA_STOP)
                    ).build()
                )
                .setContentIntent(
                    PendingIntent.getActivity(
                        baseContext,
                        0,
                        Intent(baseContext, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        },
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setContentTitle("음악재생")
                .setContentText("음원이 재생 중 입니다.")
                .build()
        } else {
            Notification.Builder(baseContext).setStyle(
                Notification.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .addAction(
                    Notification.Action.Builder(
                        R.drawable.ic_baseline_pause_24, "pause", createPendingIntent(MEDIA_PAUSE)
                    ).build()
                )
                .addAction(
                    Notification.Action.Builder(
                        R.drawable.ic_baseline_play_arrow_24, "play", createPendingIntent(MEDIA_PLAY)
                    ).build()
                )
                .addAction(
                    Notification.Action.Builder(
                        R.drawable.ic_baseline_stop_24, "stop", createPendingIntent(MEDIA_STOP)
                    ).build()
                )
                .setContentIntent(
                    PendingIntent.getActivity(
                        baseContext,
                        0,
                        Intent(baseContext, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        },
                        PendingIntent.FLAG_ONE_SHOT
                    )
                )
                .setContentTitle("음악재생")
                .setContentText("음원이 재생 중 입니다.")
                .build()
        }
        return mediaNotification
    }

    private fun initReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        registerReceiver(receiver,intentFilter)
    }

}