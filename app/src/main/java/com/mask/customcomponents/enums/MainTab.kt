package com.mask.customcomponents.enums

/**
 * 底部 Tab
 *
 * Create by lishilin on 2025-07-25
 */
enum class MainTab(val tabName: String) {

    ViewPagerNormalHint("VP_N_H"), // ViewPager，使用 FragmentPagerAdapter，使用 BEHAVIOR_SET_USER_VISIBLE_HINT
    ViewPagerNormalLifecycle("VP_N_L"), // ViewPager，使用 FragmentPagerAdapter，使用 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ViewPagerStateHint("VP_S_H"), // ViewPager，使用 FragmentStatePagerAdapter，使用 BEHAVIOR_SET_USER_VISIBLE_HINT
    ViewPagerStateLifecycle("VP_S_L"), // ViewPager，使用 FragmentStatePagerAdapter，使用 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ;

    companion object {

        val DEFAULT = ViewPagerNormalHint

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
