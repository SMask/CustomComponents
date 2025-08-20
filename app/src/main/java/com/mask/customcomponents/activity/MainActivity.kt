package com.mask.customcomponents.activity

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.R
import com.mask.customcomponents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setListener()
        initData()
    }

    private fun initView() {
        binding.svContent.post {
            binding.svContent.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun setListener() {
    }

    private fun initData() {
    }

    fun onViewClick(view: View) {
        when (view.id) {
            R.id.btn_line_chart_view -> {
                LineChartViewActivity.startActivity(this)
            }

            R.id.btn_curve_chart_view -> {
                CurveChartViewActivity.startActivity(this)
            }

            R.id.btn_password_input_view -> {
                PasswordInputViewActivity.startActivity(this)
            }

            R.id.btn_clean_edit_text -> {
                CleanEditTextActivity.startActivity(this)
            }

            R.id.btn_credit_view -> {
                CreditViewActivity.startActivity(this)
            }

            R.id.btn_ripple_view -> {
                RippleViewActivity.startActivity(this)
            }

            R.id.btn_indicator_view -> {
                IndicatorViewActivity.startActivity(this)
            }

            R.id.btn_particle_progress_bar -> {
                ParticleProgressBarActivity.startActivity(this)
            }

            R.id.btn_caliper_view -> {
                CaliperViewActivity.startActivity(this)
            }

            R.id.btn_number_view -> {
                NumberViewActivity.startActivity(this)
            }

            R.id.btn_number_text_animator -> {
                NumberTextAnimatorActivity.startActivity(this)
            }

            R.id.btn_color_picker_view -> {
                ColorPickerViewActivity.startActivity(this)
            }

            R.id.btn_gradient_rotate_view -> {
                GradientRotateViewActivity.startActivity(this)
            }

            R.id.btn_round_bevel_rect_view -> {
                RoundBevelRectActivity.startActivity(this)
            }

            R.id.btn_alphabet_index_bar -> {
                AlphabetIndexBarActivity.startActivity(this)
            }

            R.id.btn_draggable_view -> {
                DraggableViewActivity.startActivity(this)
            }

            R.id.btn_title_bar_layout -> {
                TitleBarLayoutActivity.startActivity(this)
            }

            R.id.btn_fragment_visibility -> {
                FragmentVisibilityActivity.startActivity(this)
            }

            R.id.btn_count_down_timer -> {
                CountDownTimerActivity.startActivity(this)
            }
        }
    }
}
