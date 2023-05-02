package com.example.storyappsubmission.views

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyappsubmission.R

class PasswordEditText : AppCompatEditText {
    var isValid = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        hint = context.getString(R.string.insert_your_password)
        val iconDrawable = AppCompatResources.getDrawable(context, R.drawable.baseline_lock_24)
        setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null)
        val paddingEndDp = 8
        compoundDrawablePadding = getDp(paddingEndDp)
        isSingleLine = true
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        background = AppCompatResources.getDrawable(context, R.drawable.et_bg)
        setPadding(getDp(8), 0, getDp(8), 0)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (!isValidPassword(password) && password.isNotEmpty()) {
                    error = context.getString(R.string.password_cannot_be_less_than_8_characters)
                    isValid = false
                } else if (isValidPassword(password)) {
                    error = null
                    isValid = true
                }
            }
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        invalidate()
    }

    private fun getDp(value: Int): Int {
        val density = context.resources.displayMetrics.density
        return (value * density).toInt()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 7
    }
}