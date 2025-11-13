package com.mask.customcomponents.widget.provider

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mask.customcomponents.App
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.utils.CommonUtils
import com.mask.customcomponents.utils.LogUtil
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

        val componentName: ComponentName
            get() {
                return ComponentName(App.context, TimeAppWidgetProvider::class.java)
            }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
//        printLog(context, intent)
        if (context == null) {
            super.onReceive(context, intent)
            return
        }
        when (intent?.action) {
            ACTION_REFRESH -> {
                AppWidgetHelper.updateAppWidget(context, "Widget Btn Broadcast")
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
        printLog(context, appWidgetManager, appWidgetIds)

        if (context == null) {
            return
        }
        AppWidgetHelper.updateAppWidget(context, "Provider onUpdate")

        AppWidgetWorker.enqueueRefreshWork(context)

        AppWidgetWorker.enqueuePeriodicWork(context) // 为了防止周期任务没加上，这里再添加一次，底层逻辑不会重复添加任务。
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        printLog(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        printLog(context, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        printLog(context)
        if (context == null) {
            return
        }
        AppWidgetWorker.enqueuePeriodicWork(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        printLog(context)
        if (context == null) {
            return
        }
        AppWidgetWorker.cancelPeriodicWork(context)
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        printLog(context, oldWidgetIds, newWidgetIds)
    }

    private fun printLog(vararg extraMsgArr: Any?) {
        val content = StringBuilder(Global.Tag.APP_WIDGET).append(" ")

        // 方法名
        val callerMethodName = CommonUtils.getCallerMethodName(1)
        content.append(callerMethodName.padEnd(25)).append(" : ")

        // 额外信息
        extraMsgArr.forEachIndexed { index, extraMsg ->
            if (index > 0) {
                content.append(" ; ")
            }
            val extraMsgStr = when (extraMsg) {
                is IntArray -> {
                    extraMsg.contentToString()
                }

                else -> {
                    extraMsg?.toString()
                }
            }
            content.append(extraMsgStr)
        }

        LogUtil.i(content.toString())
    }
}