package com.mask.customcomponents.enums

/**
 * 底部 Tab
 *
 * Create by lishilin on 2025-07-25
 */
enum class MainTab(val tabName: String) {

    ViewPagerNormalHint("VPNH"), // ViewPager，使用 FragmentPagerAdapter，使用 BEHAVIOR_SET_USER_VISIBLE_HINT
    ViewPagerNormalLifecycle("VPNL"), // ViewPager，使用 FragmentPagerAdapter，使用 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ViewPagerStateHint("VPSH"), // ViewPager，使用 FragmentStatePagerAdapter，使用 BEHAVIOR_SET_USER_VISIBLE_HINT
    ViewPagerStateLifecycle("VPSL"), // ViewPager，使用 FragmentStatePagerAdapter，使用 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    Normal("Normal"), // 普通 Fragment，不使用特殊容器
    ViewPager2("VP2"), // ViewPager2
    ;

    companion object {

        val DEFAULT = Normal

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
