package com.heliushouse.observerandroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.heliushouse.observerandroid.model.DownloadableItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DownloadService : Service() {
    lateinit var builder: NotificationCompat.Builder
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        startForegroundService()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDownloading()
        }
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                "my_service",
                "Observer test Service", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotification(): NotificationCompat.Builder {
        return if (this::builder.isInitialized) {
            builder
        } else {
            builder = NotificationCompat.Builder(this, "my_service")
            builder.setWhen(System.currentTimeMillis())
            builder.setSmallIcon(R.mipmap.ic_launcher)
            val largeIconBitmap =
                BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
            builder.setLargeIcon(largeIconBitmap)
            builder.setContentTitle("Observer Test")
            builder.setProgress(100, 0, true)
            builder.priority = Notification.PRIORITY_MAX
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            builder.setVibrate(null)
            builder
        }
    }

    private fun startForegroundService(downloadStatus: String = "") {
        startForeground(1572, getNotification().build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startDownloading() {
        val downloadableItem = DownloadableItem("MojoJojo", 0)
        (0..100).asFlow()
            .onEach {
                delay(1000)
                Downloads.downloadStatus.emit(
                    downloadableItem.copy(downloadPercentage = it)
                )
            }
            .launchIn(scope)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


}