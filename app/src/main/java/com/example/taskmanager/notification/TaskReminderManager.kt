package com.example.taskmanager.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.taskmanager.data.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskReminderManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun scheduleReminder(task: Task) {
        if (task.dueDate.isEmpty() || task.dueTime.isEmpty()) return

        try {
            val taskDateTime = LocalDateTime.parse(
                "${task.dueDate} ${task.dueTime}",
                DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
            )
            
            val now = LocalDateTime.now()
            val delay = Duration.between(now, taskDateTime)

            if (delay.isNegative) return

            val data = Data.Builder()
                .putInt("taskId", task.taskId)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(delay)
                .setInputData(data)
                .build()

            workManager.enqueueUniqueWork(
                "task_reminder_${task.taskId}",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelReminder(taskId: Int) {
        workManager.cancelUniqueWork("task_reminder_$taskId")
    }
}