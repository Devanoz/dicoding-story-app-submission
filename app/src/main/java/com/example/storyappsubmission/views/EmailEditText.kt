package com.example.storyappsubmission.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyappsubmission.R

class EmailEditText : AppCompatEditText {

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
        hint = context.getString(R.string.insert_your_email)
        val iconDrawable = AppCompatResources.getDrawable(context, R.drawable.baseline_email_24)
        setCompoundDrawablesRelativeWithIntrinsicBounds(iconDrawable, null, null, null)
        val paddingEndDp = 8
        compoundDrawablePadding = getDp(paddingEndDp)
        isSingleLine = true
        background = AppCompatResources.getDrawable(context, R.drawable.et_bg)
        setPadding(getDp(8), 0, getDp(8), 0)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                setCompoundDrawablesRelativeWithIntrinsicBounds(iconDrawable, null, null, null)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (!isValidEmail(email) && email.isNotEmpty()) {
                    error = context.getString(R.string.email_is_not_valid)
                    isValid = false
                } else if (isValidEmail(email)) {
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

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}