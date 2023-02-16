package com.ijp.colorpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HSVColor : Fragment() {
    private var newColor = 0
    private var oldColor = 0
    private var onColorChangedListener: OnColorChangedListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.hsv_color_picker_fragment, container, false)
        val colorPicker: ColorPicker =
            view.findViewById<View>(R.id.colorPicker) as ColorPicker
        if (newColor == 0) {
            colorPicker.setNewColor(oldColor)
        } else {
            colorPicker.setNewColor(newColor)
        }
        colorPicker.setOnColorChangeListener(onColorChangedListener)
        return view
    }

    companion object {
        fun newInstance(
            newColor: Int,
            oldColor: Int,
            onColorChangedListener: OnColorChangedListener?
        ): HSVColor {
            val hsvColor = HSVColor()
            hsvColor.newColor = newColor
            hsvColor.onColorChangedListener = onColorChangedListener
            hsvColor.oldColor = oldColor
            return hsvColor
        }
    }
}