package com.ijp.modules

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.ijp.colorpicker.ColorDialog
import com.ijp.colorpicker.OnColorChangedListener

import com.ijp.modules.databinding.ActivityMainBinding
import com.ijp.segments.SegmentColorFragment
import com.ijp.segments.model.RangeBarArray

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val segmnt = SegmentColorFragment().apply {
            this.updateList(
                mutableListOf(
                    RangeBarArray(
                        0,
                        20,
                        Color.parseColor("#12fe45")
                    ),
                    RangeBarArray(
                        21,
                        60,
                        Color.parseColor("#EE2342")
                    ),

                    RangeBarArray(
                        61,
                        100,
                        Color.parseColor("#aafcee")
                    ),

                    )
            )
        }
        segmnt.setOnValueChange {
            it.forEach { ra->
                Log.d("RangeArr:","${ra.start} ${ra.end} ${ra.color}")
            }
        }
        loadFragment(
            segmnt
        )
        binding?.btnOpenDialog?.setOnClickListener {

            val colorDialog =
                ColorDialog.newInstance(
                    0,
                    object : OnColorChangedListener {
                        override fun colorChanged(color: Int) {

                            Log.d("Color:", "$color")
                            binding?.btnOpenDialog?.backgroundTintList =
                                ColorStateList.valueOf(color)
                        }
                    }
                )
            colorDialog?.show(supportFragmentManager, "Main")

        }
    }

    private fun loadFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.color_fragment_container, fragment)
        transaction.commitNow()
    }
}