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

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

class NotificationSchedulerWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Get the current date and time
        val now = LocalDateTime.now()

        // Define the specific time for 8 AM
        val specificTime = LocalTime.of(8, 0) // 8:00 AM

        // Create a LocalDateTime for today at 8 AM
        val todayAtSpecificTime = now.toLocalDate().atTime(specificTime)

        // Determine the target time based on the current time
        val targetTime = if (now.isBefore(todayAtSpecificTime)) {
            todayAtSpecificTime // If current time is before 8 AM today
        } else {
            todayAtSpecificTime.plusDays(1) // If current time is after 8 AM today, go to 8 AM tomorrow
        }
            .plusHours(1)
            .plusMinutes(Random.nextLong(0, 120))

        val duration = Duration.between(now, targetTime)
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(duration)
            .addTag("Next")
            .build()

        val wm = WorkManager.getInstance(applicationContext)
        wm.cancelAllWorkByTag("Next").await()
        wm.enqueue(notificationWork)
        return Result.success()
    }
}
