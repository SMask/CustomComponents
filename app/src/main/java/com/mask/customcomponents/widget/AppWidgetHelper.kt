package com.mask.customcomponents.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.mask.customcomponents.R
import com.mask.customcomponents.activity.AppWidgetActivity
import com.mask.customcomponents.activity.MainActivity
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.widget.provider.TimeAppWidgetProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * AppWidget 桌面小组件
 *
 * Create by lishilin on 2025-11-12
 */
object AppWidgetHelper {

    private val dateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    }

    private val currentTimeFormat: String
        get() {
            return dateFormat.format(Date())
        }

    fun requestPinAppWidget(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return false
        }

        return try {
            AppWidgetManager.getInstance(context)
                .requestPinAppWidget(TimeAppWidgetProvider.componentName, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun updateAppWidget(context: Context, sourceTag: String) {
        LogUtil.i("${Global.Tag.APP_WIDGET} updateAppWidget: $sourceTag")

        val remoteViews = RemoteViews(context.packageName, R.layout.layout_app_widget_time)
        remoteViews.setTextViewText(R.id.tv_time, currentTimeFormat)
        remoteViews.setTextViewText(R.id.tv_source, sourceTag)

        val openPendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, AppWidgetActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.btn_open, openPendingIntent)

        val refreshIntent = Intent()
            .setComponent(TimeAppWidgetProvider.componentName)
            .setAction(TimeAppWidgetProvider.ACTION_REFRESH)
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.btn_refresh, refreshPendingIntent)

        val rootPendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.layout_root, rootPendingIntent)

        AppWidgetManager.getInstance(context)
            .updateAppWidget(TimeAppWidgetProvider.componentName, remoteViews)
    }

}