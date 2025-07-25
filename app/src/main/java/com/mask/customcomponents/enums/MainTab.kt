package com.mask.customcomponents.enums

/**
 * 底部 Tab
 *
 * Create by lishilin on 2025-07-25
 */
enum class MainTab(val tabName: String) {

    TAB_1("Tab_1"), // 我的（我的消息、我的生意 父容器）
    TAB_2("Tab_2"), // 关注
    TAB_3("Tab_3"), // 发布
    TAB_4("Tab_4"), // 找同行（首页）
    ;

    companion object {

        val DEFAULT = TAB_1

        fun getInstance(tabName: String?, defaultTab: MainTab? = null): MainTab {
            entries.forEach { item ->
                if (tabName == item.tabName) {
                    return item
                }
            }
            return defaultTab ?: DEFAULT
        }

        fun getInstance(index: Int, defaultTab: MainTab? = null): MainTab {
            return entries.getOrElse(index) { defaultTab ?: DEFAULT }
        }
    }
}
