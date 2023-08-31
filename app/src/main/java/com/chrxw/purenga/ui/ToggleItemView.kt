package com.chrxw.purenga.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.Switch
import com.chrxw.purenga.utils.Helper

class ToggleItemView(context: Context, spKey: String) : ClickableItemView(context), OnClickListener {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private val switch = Switch(context)
    private var spKey: String = ""

    init {
        this.spKey = spKey
        switch.isChecked = Helper.getSpBool(spKey, false)

        val switchParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
        }
        switch.layoutParams = switchParams

        this.containerLayout.setOnClickListener(this)
        this.titleTextView.setOnClickListener(this)
        this.subTextView.setOnClickListener(this)
        this.switch.setOnClickListener(this)
        this.addView(switch)
    }

    constructor(context: Context) : this(context, "")

    private var isChecked: Boolean
        get() = switch.isChecked
        set(value) {
            switch.isChecked = value
            Helper.spPlugin.edit().putBoolean(spKey, isChecked).apply()
        }

    override fun onClick(v: View?) {
        isChecked = if (v !is Switch) {
            !isChecked
        } else {
            switch.isChecked
        }
    }
}