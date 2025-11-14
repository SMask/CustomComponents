package com.mask.customcomponents.widget.provider

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mask.customcomponents.App
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.utils.LogHelper
import com.mask.customcomponents.widget.AppWidgetHelper
import com.mask.customcomponents.widget.worker.AppWidgetWorker

/**
 * AppWidget 桌面小组件 Time
 *
 * Create by lishilin on 2025-11-12
 */
class TimeAppWidgetProvider : AppWidgetProvider() {

    companion object {

        const val ACTION_REFRESH = "com.mask.customcomponents.widget.action.REFRESH"
        const val ACTION_REFRESH_WORK_MANAGER =
            "com.mask.customcomponents.widget.action.REFRESH_WORK_MANAGER"

        val componentName: ComponentName
            get() {
                return ComponentName(App.context, TimeAppWidgetProvider::class.java)
            }

        val appWidgetIds: IntArray
            get() {
                return AppWidgetManager.getInstance(App.context).getAppWidgetIds(componentName)
            }

        val instanceCount: Int
            get() {
                return appWidgetIds.size
            }

        val hasInstance: Boolean
            get() {
                return instanceCount > 0
            }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
//        LogHelper.i(Global.Tag.APP_WIDGET, context, intent)
        if (context == null) {
            super.onReceive(context, intent)
            return
        }
        when (intent?.action) {
            ACTION_REFRESH -> {
                AppWidgetHelper.updateAppWidget(context, "Widget Btn Broadcast")
            }

            ACTION_REFRESH_WORK_MANAGER -> {
                AppWidgetWorker.enqueueRefreshWork(context, "Widget Btn Broadcast")
            }

            else -> {
                super.onReceive(context, intent)
            }
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        LogHelper.i(Global.Tag.APP_WIDGET, context, appWidgetManager, appWidgetIds)

        if (context == null) {
            return
        }
//        AppWidgetHelper.updateAppWidget(context, "Provider onUpdate")

        AppWidgetWorker.enqueueRefreshWork(context, "Provider onUpdate")

        // 为了防止周期任务没加上，这里再添加一次，底层逻辑不会重复添加任务。
        AppWidgetWorker.enqueuePeriodicWork(context, "Provider onUpdate")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        LogHelper.i(Global.Tag.APP_WIDGET, context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        LogHelper.i(Global.Tag.APP_WIDGET, context, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        LogHelper.i(Global.Tag.APP_WIDGET, context)
        if (context == null) {
            return
        }
        AppWidgetWorker.enqueuePeriodicWork(context, "Provider onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        LogHelper.i(Global.Tag.APP_WIDGET, context)
        if (context == null) {
            return
        }
        AppWidgetWorker.cancelPeriodicWork(context, "Provider onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        LogHelper.i(Global.Tag.APP_WIDGET, context, oldWidgetIds, newWidgetIds)
    }
}