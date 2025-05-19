package com.mask.customcomponents.utils

import android.content.Context
import android.widget.Toast
import com.mask.customcomponents.App

/**
 * ToastUtils
 *
 * Create by lishilin on 2025-04-30
 */
object ToastUtils {

    fun show(msg: String) {
        show(App.context, msg)
    }

    fun show(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}