package com.clint.nasa.core.view.text

import android.content.Context
import android.util.AttributeSet
import com.clint.nasa.R
import com.google.android.material.textview.MaterialTextView

class NasaTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MaterialTextView(context, attributeSet, defStyleAttr) {
    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.NasaTextView)
        val typography = Typography.from(typedArray.getInt(R.styleable.NasaTextView_typography, 0))
        setTypography(typography)
        typedArray.recycle()
    }

    private fun setTypography(typography: Typography) {
        setTextAppearance(Typography.getTextAppearanceStyle(typography))
    }

    enum class Typography {
        H1, H2, H3, H4, Body;

        companion object {
            fun from(type: Int): Typography {
                return when (type) {
                    0 -> H1
                    1 -> H2
                    2 -> H3
                    3 -> H4
                    4 -> Body
                    else -> Body
                }
            }

            fun getTextAppearanceStyle(typography: Typography): Int {
                return when (typography) {
                    H1 -> R.style.Typography_Nasa_H1
                    H2 -> R.style.Typography_Nasa_H2
                    H3 -> R.style.Typography_Nasa_H3
                    H4 -> R.style.Typography_Nasa_H4
                    Body -> R.style.Typography_Nasa_Body
                }
            }

        }

    }
}