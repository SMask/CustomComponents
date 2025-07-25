package com.mask.customcomponents.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.mask.customcomponents.enums.MainTab

/**
 * Create by lishilin on 2025-07-25
 */
class FragmentVisibilityViewModel : ViewModel() {

    // 当前选中的 Tab
    private val _selectedTab = MutableLiveData<MainTab?>()
    val selectedTab: LiveData<MainTab?> = _selectedTab.distinctUntilChanged()

    fun setSelectedTab(tab: MainTab) {
        _selectedTab.value = tab
    }

}