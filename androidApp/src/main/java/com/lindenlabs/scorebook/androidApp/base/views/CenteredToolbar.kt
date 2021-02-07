package com.lindenlabs.scorebook.androidApp.base.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.Nullable

class CenteredToolbar : Toolbar {
    private var centeredTitleTextView: TextView? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setTitle(title: CharSequence?) {
        title?.let { getCenteredTitleTextView().text = it}
    }

    override fun getTitle(): CharSequence?
        = getCenteredTitleTextView().text.toString()

    private fun getCenteredTitleTextView(): TextView {
        if (centeredTitleTextView == null) {
            centeredTitleTextView = TextView(getContext())
            centeredTitleTextView!!.setSingleLine()
            centeredTitleTextView!!.ellipsize = TextUtils.TruncateAt.END
            centeredTitleTextView!!.gravity = Gravity.CENTER
            centeredTitleTextView!!.setTextAppearance(
                context,
                android.R.style.TextAppearance_Material_Widget_ActionBar_Title)
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.CENTER
            centeredTitleTextView!!.layoutParams = lp
            addView(centeredTitleTextView)
        }
        return centeredTitleTextView!!
    }
}