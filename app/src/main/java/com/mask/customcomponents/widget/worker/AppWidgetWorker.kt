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
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.utils.LogHelper
import com.mask.customcomponents.widget.AppWidgetHelper
import java.util.concurrent.TimeUnit

/**
 * AppWidget 桌面小组件 Worker
 *
 * Create by lishilin on 2025-11-13
 */
class AppWidgetWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {

        private const val WORK_NAME_UNIQUE = "app_widget_worker_unique"

        private const val KEY_SOURCE_TAG = "source_tag"

        /**
         * 添加 刷新任务
         * 期望立即执行
         */
        fun enqueueRefreshWork(context: Context, sourceTag: String) {
            LogHelper.i(Global.Tag.APP_WIDGET, context, sourceTag)

            val request = OneTimeWorkRequestBuilder<AppWidgetWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(Constraints.NONE)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .setInputData(workDataOf(KEY_SOURCE_TAG to "$sourceTag OneTimeWork"))
                .build()
            WorkManager.getInstance(context.applicationContext).enqueue(request)
        }

        /**
         * 添加 周期循环任务
         */
        fun enqueuePeriodicWork(context: Context, sourceTag: String) {
            LogHelper.i(Global.Tag.APP_WIDGET, context, sourceTag)

            val request = PeriodicWorkRequestBuilder<AppWidgetWorker>(
                15, TimeUnit.MINUTES,
                5, TimeUnit.MINUTES
            )
                .setConstraints(Constraints.NONE)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .setInitialDelay(15, TimeUnit.MINUTES)
                .setInputData(workDataOf(KEY_SOURCE_TAG to "$sourceTag PeriodicWork"))
                .build()
            WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
                WORK_NAME_UNIQUE, ExistingPeriodicWorkPolicy.UPDATE, request
            )
        }

        /**
         * 取消 周期循环任务
         */
        fun cancelPeriodicWork(context: Context, sourceTag: String) {
            LogHelper.i(Global.Tag.APP_WIDGET, context, sourceTag)

            WorkManager.getInstance(context.applicationContext).cancelUniqueWork(WORK_NAME_UNIQUE)
        }
    }

    override fun doWork(): Result {
        val sourceTag = inputData.getString(KEY_SOURCE_TAG)
        AppWidgetHelper.updateAppWidget(applicationContext, "$sourceTag doWork")
        return Result.success()
    }
}