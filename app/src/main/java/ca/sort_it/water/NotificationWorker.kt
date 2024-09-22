/*
 *  Copyright (c) 2024 danielzhang130
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.sort_it.water

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        showNotification()
        scheduleNext()
        return Result.success()
    }

    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "notification_channel_id"

        val channel = NotificationChannel(
            channelId,
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.water_drop)
            .setContentTitle("Reminder to drink water")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            )

        notificationManager.notify(Random.nextInt(), notification.build())
    }

    private suspend fun scheduleNext() {
        val now = LocalDateTime.now()
        val cutoffTime = LocalTime.of(22, 0)

        val targetTime = now
            .plusHours(1)
            .plusMinutes(Random.nextLong(0, 120))

        if (targetTime.isAfter(now.toLocalDate().atTime(cutoffTime))) {
            return
        }

        val duration = Duration.between(now, targetTime)
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .addTag("Next")
            .setInitialDelay(duration)
            .build()

        val wm = WorkManager.getInstance(applicationContext)
        wm.cancelAllWorkByTag("Next").await()
        wm.enqueue(notificationWork)
    }
}
