package com.example.taskmanager.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskmanager.data.TaskDatabase

class TaskReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val taskId = inputData.getInt("taskId", -1)
        if (taskId == -1) return androidx.work.ListenableWorker.Result.failure()

        val database = TaskDatabase.getDatabase(applicationContext)
        val task = database.taskDao().getTaskById(taskId)

        return if (task != null) {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.showTaskNotification(task)
            androidx.work.ListenableWorker.Result.success()
        } else {
            androidx.work.ListenableWorker.Result.failure()
        }
    }
}