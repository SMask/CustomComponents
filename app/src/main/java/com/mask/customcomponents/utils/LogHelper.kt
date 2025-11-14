package com.mask.customcomponents.utils

/**
 * Log
 *
 * Create by lishilin on 2025-11-14
 */
object LogHelper {

    fun i(tag: String, vararg extraMsgArr: Any?) {
        val content = StringBuilder(tag).append(" ")

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