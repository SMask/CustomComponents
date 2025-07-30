package com.mask.customcomponents.enums

/**
 * 底部 Tab
 *
 * Create by lishilin on 2025-07-25
 */
enum class MainTab(val tabName: String) {

    ViewPagerNormalHint("VPNH"), // ViewPager，使用 FragmentPagerAdapter，使用 BEHAVIOR_SET_USER_VISIBLE_HINT
    ViewPagerNormalLifecycle("VPNL"), // ViewPager，使用 FragmentPagerAdapter，使用 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    Normal1("N.1"), // 普通 Fragment，不使用特殊容器
    ViewPagerStateHint("VPSH"), // ViewPager，使用 FragmentStatePagerAdapter，使用 BEHAVIOR_SET_USER_VISIBLE_HINT
    ViewPagerStateLifecycle("VPSL"), // ViewPager，使用 FragmentStatePagerAdapter，使用 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    Normal2("N.2"), // 普通 Fragment，不使用特殊容器
    ViewPager2("VP2"), // ViewPager2
    Normal3("N.3"), // 普通 Fragment，不使用特殊容器
    ;

    companion object {

        val DEFAULT = Normal1

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
