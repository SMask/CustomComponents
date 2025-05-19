package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mask.customcomponents.adapter.UserAdapter
import com.mask.customcomponents.databinding.ActivityAlphabetIndexBarBinding
import com.mask.customcomponents.utils.SizeUtils
import com.mask.customcomponents.view.DividerItemDecoration
import com.mask.customcomponents.vo.UserVo

/**
 * 字母索引栏(仿微信通讯录)
 */
class AlphabetIndexBarActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAlphabetIndexBarBinding.inflate(layoutInflater)
    }

    private val userList by lazy {
        val nameArr = arrayOf(
            "的工", "一老", "是育", "了西", "我北", "不广", "在觉", "人解", "们意", "有政",
            "来正", "他度", "这手", "上结", "着想", "个者", "地体", "到合", "你斗", "说路",
            "生通", "国门", "和入", "子强", "就先", "年山", "得治", "要代", "以合", "出名",
            "会利", "可水", "也内", "对相", "她由", "过口", "后月", "之化", "为军", "都公",
            "而事", "样马", "自情", "去立", "学处", "进世", "好市", "小展", "心间", "前方",
            "发再", "看等", "起育", "还海", "用西", "道南", "行北", "所广", "然觉", "家解",
            "种意", "事政", "成正", "方度", "多手", "经结", "么想", "去者", "法体", "当合",
            "天斗", "如路", "分通", "想门", "能入", "主强", "将先", "外山", "但治", "些代",
            "把合", "见名", "次利", "现水", "身内", "己相", "又由", "平口", "动月", "两化",
            "知军", "明公", "日事", "间马", "真情", "实立", "力处", "样世", "长市", "见展",
            "把合何合", "把合何", "把合何1", "把合何2", "把合何11", "把合何12"
        )
        val userList = mutableListOf<UserVo>()
        nameArr.forEachIndexed { index, data ->
            userList.add(UserVo(index.toString(), data))
        }
        userList
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AlphabetIndexBarActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setListener()
        initData()
    }

    private fun initView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.addItemDecoration(DividerItemDecoration.getInstance(SizeUtils.dpToPx(0.5f).toInt(), Color.LTGRAY))
    }

    private fun setListener() {
    }

    private fun initData() {
        binding.rvUser.adapter = UserAdapter(userList)
    }
}
