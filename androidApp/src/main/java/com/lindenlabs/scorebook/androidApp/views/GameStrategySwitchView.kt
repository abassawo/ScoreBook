package com.lindenlabs.scorebook.androidApp.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameStrategySwitchBinding

class GameStrategySwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Checkable {

//    private var binding: GameStrategySwitchBinding = GameStrategySwitchBinding.inflate(LayoutInflater.from(context))


    init {
        val view = inflate(context, R.layout.game_strategy_switch, null)
        this.addView(view)

    }


    fun GameStrategySwitchBinding.bind() =
        with(gameRuleSwitch) {
            textOff = context.getString(R.string.high_score)
            textOn = context.getString(R.string.low_score)
        }

    override fun setChecked(checked: Boolean) {
//        binding.gameRuleSwitch.isChecked = checked
    }

    override fun isChecked(): Boolean =  true // binding.gameRuleSwitch.isChecked


    override fun toggle() = Unit // binding.gameRuleSwitch.toggle()
}
