package com.ijp.colorpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HEXColor : Fragment() {
    private var newColor = 0
    private var oldColor = 0
    private var onColorChangedListener: OnColorChangedListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val hexSelectorView = HexSelectorView(context, newColor, oldColor)
        hexSelectorView.setOnColorChangedListener(onColorChangedListener)
        return hexSelectorView
    }

    companion object {
        fun newInstance(
            newColor: Int,
            oldColor: Int,
            onColorChangedListener: OnColorChangedListener?
        ): HEXColor {
            val hexColor = HEXColor()
            hexColor.newColor = newColor
            hexColor.onColorChangedListener = onColorChangedListener
            hexColor.oldColor = oldColor
            return hexColor
        }
    }
}
