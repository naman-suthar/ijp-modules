package com.ijp.colorpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RGBColor : Fragment() {
    private var newColor = 0
    private var oldColor = 0
    private var onColorChangedListener: OnColorChangedListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rgbSelectorView = RgbSelectorView(context, newColor, oldColor)
        rgbSelectorView.setOnColorChangedListener(onColorChangedListener)
        return rgbSelectorView
    }

    companion object {
        fun newInstance(
            newColor: Int,
            oldColor: Int,
            onColorChangedListener: OnColorChangedListener?
        ): RGBColor {
            val rgbColor = RGBColor()
            rgbColor.newColor = newColor
            rgbColor.onColorChangedListener = onColorChangedListener
            rgbColor.oldColor = oldColor
            return rgbColor
        }
    }
}
