package com.lindenlabs.scorebook.androidApp.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.lindenlabs.scorebook.androidApp.databinding.GameStrategySwitchBinding


class GameStrategySwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: GameStrategySwitchBinding

    init {
        with(GameStrategySwitchBinding.inflate(LayoutInflater.from(context))) {
            binding = this
            binding.setup()
            addView(root)
        }

    }

    fun isLowestScoreStrategyChecked(): Boolean = binding.gameRuleSwitch.isChecked

    fun setLowestScoreStrategyGameEnabled(enabled: Boolean) {
        binding.gameRuleSwitch.isChecked = enabled
    }

    fun GameStrategySwitchBinding.setup() =
        with(gameRuleSwitch) {
            textOff = context.getString(R.string.high_score)
            textOn = context.getString(R.string.low_score)
        }
}
