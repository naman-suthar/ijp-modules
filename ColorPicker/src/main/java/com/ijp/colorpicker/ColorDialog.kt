package com.ijp.colorpicker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class ColorDialog : DialogFragment(),
    OnColorChangedListener {
    private var oldColor = 0
    private var newColor = 0
    private lateinit var onColorChangedListener: OnColorChangedListener
    lateinit var newButton: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.color_dialog, null, false)
        val colorPager: StaticViewPager =
            v.findViewById<View>(R.id.colorPager) as StaticViewPager
        val oldButton = v.findViewById<TextView>(R.id.oldButton)

        //Log.v("COLORME",oldColor+"");
        oldButton.setBackgroundColor(oldColor)
        //Log.v("oldColor", "" + oldColor);
        oldButton.setOnClickListener { dismiss() }
        newButton = v.findViewById(R.id.newButton)
        newButton.setBackgroundColor(oldColor)
        newButton.setOnClickListener(View.OnClickListener {
            onColorChangedListener.colorChanged(newColor)
            dismiss()
        })
        val dialogTabLayout: TabLayout = v.findViewById(R.id.dialogTabLayout)

        dialogTabLayout.setTabTextColors(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            ), ContextCompat.getColor(requireContext(), R.color.black)
        )
        dialogTabLayout.addTab(
            dialogTabLayout.newTab().setCustomView(R.layout.hsv_tab)
        ) //setText(getString(R.string.HSV_TITLE)).setIcon(R.drawable.hsv), false);
        dialogTabLayout.addTab(dialogTabLayout.newTab().setCustomView(R.layout.rgb_tab))
        dialogTabLayout.addTab(dialogTabLayout.newTab().setCustomView(R.layout.hex_tab))
        val viewPagerAdapater =
            ViewPagerAdapater(childFragmentManager)
        colorPager.setAdapter(viewPagerAdapater)
        dialogTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                colorPager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return v
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun colorChanged(color: Int) {
        newColor = color
        newButton.setBackgroundColor(color)
    }

    internal inner class ViewPagerAdapater(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return HSVColor.newInstance(newColor, oldColor, this@ColorDialog)
                1 -> return RGBColor.newInstance(newColor, oldColor, this@ColorDialog)
                2 -> return HEXColor.newInstance(newColor, oldColor, this@ColorDialog)
            }
            return Fragment()
        }

        override fun getCount(): Int {
            return 3
        }
    }

    companion object {
        fun newInstance(
            oldColor: Int,
            onColorChangedListener: OnColorChangedListener
        ): ColorDialog {
            val f = ColorDialog()
            f.oldColor = oldColor
            f.newColor = oldColor
            f.onColorChangedListener = onColorChangedListener
            return f
        }
    }
}