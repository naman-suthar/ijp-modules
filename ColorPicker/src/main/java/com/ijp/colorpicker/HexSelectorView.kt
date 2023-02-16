package com.ijp.colorpicker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

class HexSelectorView : LinearLayout {
    private var edit: EditText? = null
    private var color = 0
    private var listener: OnColorChangedListener? = null

    constructor(context: Context?, newColor: Int, oldcolor: Int) : super(context) {
        init(newColor, oldcolor)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        newColor: Int,
        oldcolor: Int
    ) : super(context, attrs) {
        init(newColor, oldcolor)
    }

    private fun init(newColor: Int, oldColor: Int) {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content: View = inflater.inflate(R.layout.color_hexview, null)
        //txtError = (TextView)content.findViewById(R.id.color_hex_txtError);
        addView(
            content,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
        edit = content.findViewById<View>(R.id.color_hex_edit) as EditText
        if (newColor == 0) {
            setColor(oldColor)
        } else {
            setColor(newColor)
        }
        val btnSave =
            content.findViewById<View>(R.id.color_hex_btnSave) as Button
        btnSave.setOnClickListener {
            val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edit!!.windowToken, 0)
            try {
                var hex = edit!!.text.toString()
                //					String prefix = "";
                /* if (hex.startsWith("0x")) {
                                hex = hex.substring(2);
        //						prefix = "0x";
                            }*/if (hex.startsWith("#")) {
                    hex = hex.substring(1)
                    //						prefix = "#";
                }
                if (hex.length == 6) {
                    hex = "FF$hex"
                }
                if (hex.length != 8) throw Exception()
                color = hex.toLong(16).toInt()
                onColorChanged()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.hex_code_picker),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getColor(): Int {
        return color
    }

    private fun setColor(color: Int) {
        if (color == this.color) return
        this.color = color
        //edit.setText(padLeft(Integer.toHexString(color).toUpperCase(), '0', 6));
        try {
            edit!!.setText(Integer.toHexString(color).substring(2).toUpperCase())
        } catch (siobe: StringIndexOutOfBoundsException) {
        }
    }

    /*private String padLeft(String string, char padChar, int size) {
        if (string.length() >= size)
            return string;
        StringBuilder result = new StringBuilder();
        for (int i = string.length(); i < size; i++)
            result.append(padChar);
        result.append(string);
        return result.toString();
    }*/
    private fun onColorChanged() {
        if (listener != null) listener!!.colorChanged(getColor())
    }

    fun setOnColorChangedListener(listener: OnColorChangedListener?) {
        this.listener = listener
    }
}
