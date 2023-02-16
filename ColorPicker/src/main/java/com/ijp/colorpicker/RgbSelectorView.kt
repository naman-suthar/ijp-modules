package com.ijp.colorpicker

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.slider.Slider

class RgbSelectorView : LinearLayout {
    private var seekRed: Slider? = null
    private var seekGreen: Slider? = null
    private var seekBlue: Slider? = null
    private var seekAlpha: Slider? = null
    private var rValue: TextView? = null
    private var gValue: TextView? = null
    private var bValue: TextView? = null
    private var aValue: TextView? = null

    //private ImageView imgPreview;
    private var listener: OnColorChangedListener? = null

    constructor(context: Context?, newColor: Int, oldColor: Int) : super(context) {
        init(newColor, oldColor)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        newColor: Int,
        oldColor: Int
    ) : super(context, attrs) {
        init(newColor, oldColor)
    }

    private fun init(newColor: Int, oldColor: Int) {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rgbView: View = inflater.inflate(R.layout.color_rgbview, null)
        rValue = rgbView.findViewById<View>(R.id.rValue) as TextView
        gValue = rgbView.findViewById<View>(R.id.gValue) as TextView
        bValue = rgbView.findViewById<View>(R.id.bValue) as TextView
        aValue = rgbView.findViewById<View>(R.id.aValue) as TextView
        addView(
            rgbView,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
        val listener: Slider.OnChangeListener = object : Slider.OnChangeListener {

            override fun onValueChange(slider: Slider, progress: Float, fromUser: Boolean) {
                when (slider.id) {
                    R.id.color_rgb_seekRed -> rValue!!.text = "" + progress.toInt()
                    R.id.color_rgb_seekGreen -> gValue!!.text = "" + progress.toInt()
                    R.id.color_rgb_seekBlue -> bValue!!.text = "" + progress.toInt()
                    R.id.color_rgb_seekAlpha -> aValue!!.text = "" + progress.toInt()
                }
                onColorChanged()
            }
        }
        seekRed = rgbView.findViewById<View>(R.id.color_rgb_seekRed) as Slider
        seekGreen = rgbView.findViewById<View>(R.id.color_rgb_seekGreen) as Slider
        seekBlue = rgbView.findViewById<View>(R.id.color_rgb_seekBlue) as Slider
        seekAlpha = rgbView.findViewById<View>(R.id.color_rgb_seekAlpha) as Slider

        //Log.v("newColor", "" + ColorDialog.newColor);
        color = if (newColor == 0) {
            oldColor
        } else {
            newColor
        }
        seekBlue!!.addOnChangeListener(listener)
        seekGreen!!.addOnChangeListener(listener)
        seekRed!!.addOnChangeListener(listener)
        seekAlpha!!.addOnChangeListener(listener)


        //imgPreview = (ImageView)rgbView.findViewById(R.id.color_rgb_imgpreview);

        //setColor(Color.BLACK);
    }

    //setPreviewImage();
    private var color: Int
        private get() = Color.argb(
            seekAlpha!!.value,
            seekRed!!.value,
            seekGreen!!.value,
            seekBlue!!.value
        )
        private set(color) {
            seekRed!!.value = Color.red(color).toFloat()
            seekGreen!!.value = Color.green(color).toFloat()
            seekBlue!!.value = Color.blue(color).toFloat()
            seekAlpha!!.value = Color.alpha(color).toFloat()
            rValue!!.text = Color.red(color).toString() + ""
            gValue!!.text = Color.green(color).toString() + ""
            bValue!!.text = Color.blue(color).toString() + ""
            aValue!!.text = Color.alpha(color).toString() + ""
            //setPreviewImage();
        }

    private fun onColorChanged() {
        if (listener != null) listener!!.colorChanged(color)
    }

    fun setOnColorChangedListener(listener: OnColorChangedListener?) {
        this.listener = listener
    }
}
