package com.capstone.plasticwise.view

import android.content.Context
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }
    }

    fun setIndentedText(text: CharSequence) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(LeadingMarginSpan.Standard(40, 0), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(spannableString, BufferType.SPANNABLE)
    }
}
