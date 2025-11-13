package com.mask.customcomponents.widget.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.mask.customcomponents.widget.AppWidgetHelper
import java.time.Duration

/**
 * AppWidget 桌面小组件 Worker
 *
 * Create by lishilin on 2025-11-13
 */
class AppWidgetWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {

        private const val WORK_NAME_UNIQUE = "app_widget_worker_unique"

        private const val KEY_SOURCE = "source"

        /**
         * 添加 刷新任务
         * 期望立即执行
         */
        fun enqueueRefreshWork(context: Context) {
            val request = OneTimeWorkRequestBuilder<AppWidgetWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(Constraints.NONE)
                .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(10))
                .setInputData(workDataOf(KEY_SOURCE to "OneTimeWork"))
                .build()
            WorkManager.getInstance(context.applicationContext).enqueue(request)
        }

        /**
         * 添加 周期循环任务
         */
        fun enqueuePeriodicWork(context: Context) {
            val request = PeriodicWorkRequestBuilder<AppWidgetWorker>(
                Duration.ofMinutes(15), Duration.ofMinutes(5)
            )
                .setConstraints(Constraints.NONE)
                .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(10))
                .setInitialDelay(Duration.ofMinutes(15))
                .setInputData(workDataOf(KEY_SOURCE to "PeriodicWork"))
                .build()
            WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
                WORK_NAME_UNIQUE, ExistingPeriodicWorkPolicy.KEEP, request
            )
        }

        /**
         * 取消 周期循环任务
         */
        fun cancelPeriodicWork(context: Context) {
            WorkManager.getInstance(context.applicationContext).cancelUniqueWork(WORK_NAME_UNIQUE)
        }
    }

    override fun doWork(): Result {
        val source = inputData.getString(KEY_SOURCE)
        AppWidgetHelper.updateAppWidget(context, "WorkManager $source doWork")
        return Result.success()
    }
}