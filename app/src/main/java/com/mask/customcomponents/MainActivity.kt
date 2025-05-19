package com.mask.customcomponents

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

            R.id.btn_color_picker_view -> {
                ColorPickerViewActivity.startActivity(this)
            }

            R.id.btn_gradient_rotate_view -> {
                GradientRotateViewActivity.startActivity(this)
            }

            R.id.btn_round_bevel_rect_view -> {
                RoundBevelRectActivity.startActivity(this)
            }
        }
    }
}
