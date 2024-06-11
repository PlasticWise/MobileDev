package com.capstone.plasticwise

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class MyEmailText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (text != null && text.contains("@gmail.com")) {
            error = null
            setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            error = "Gmail is required contains \"@gmail.com\""
            setTextColor(ContextCompat.getColor(context, R.color.md_theme_error))
        }
    }
}