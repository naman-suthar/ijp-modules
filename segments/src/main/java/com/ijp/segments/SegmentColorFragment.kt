package com.ijp.segments

import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.ijp.segments.databinding.FragmentSegmentColorBinding
import com.ijp.segments.model.RangeBarArray
import kotlinx.android.synthetic.main.each_rangebar.view.*

//private var arrayList = ArrayList<RangeBarArray>()
class SegmentColorFragment() : Fragment(),CustomComponentsCallback {
    private var arrayList = mutableListOf<RangeBarArray>()
    private var onValueChange: ((MutableList<RangeBarArray>)-> Unit)? =null

    fun setOnValueChange(onValueChangeFunction: (MutableList<RangeBarArray>)-> Unit){
        onValueChange = onValueChangeFunction
    }
    companion object {
        fun newInstance(onValueChange: (MutableList<RangeBarArray>) -> Unit): SegmentColorFragment {
            val args = Bundle()
//            args.putInt("foo", foo)
            val fragment = SegmentColorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun updateList(list: MutableList<RangeBarArray>){
        arrayList = list
        this.view?.invalidate()
    }
    fun getColorList(): MutableList<RangeBarArray>{
        return arrayList
    }
   private var binding: FragmentSegmentColorBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSegmentColorBinding.inflate(inflater,container,false)


        arrayList.forEach {
            //Creating the rangeBars from database
            binding?.barViewLayout?.removeAllViews()
            //array to remove duplicate values
            addComponents(arrayList)
            //To show the live view
            firstTimeBarLiveView()

        }

        return binding?.root
    }

    private fun addComponents(list: MutableList<RangeBarArray>) {
        binding?.rangeBarComponent?.removeAllViews()
        val lp = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        for (i in list) {

            val newCompo = binding?.rangeBarComponent?.let {
                binding?.barViewLayout?.let { it1 ->
                    context?.let { it2 ->
                        CustomComponent(
                            it2,
                            this,
                            requireActivity().supportFragmentManager,
                            binding?.barViewLayout!!,
                            binding?.rangeBarComponent!!,
                            arrayList
                        )
                    }
                }
            }
            newCompo?.setStartText(i.start)
            newCompo?.setEndText(i.end)
            newCompo?.setProgress(i.start, i.end)

            if (i.start == 0) {
                newCompo?.thicknessSlider_custom?.visibility = View.VISIBLE
                newCompo?.rangeBar?.visibility = View.INVISIBLE
            }
            newCompo?.setColor(i.color)
            newCompo?.let {nc->
                binding?.rangeBarComponent?.addView(nc, lp)
            }

        }
    }

    override fun onValueChanged() {
        onValueChange?.let {
            it(
                arrayList
            )
        }

    }


   /* override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Fragment:","OnAttach Called")
        mycontext = context
    }*/

    /*override fun onDetach() {
        super.onDetach()
        Log.d("Fragment:","OnDetach Called")
        mycontext = null
    }*/
    private fun firstTimeBarLiveView() {
        //live Bar change logic
        if(this.isAdded){
            binding?.let { b->
                b.barViewLayout.removeAllViews()
                for (i in 0 until arrayList.size) {
                    val barView = LinearLayout(binding?.root?.context)

                    val rangebarView = b.rangeBarComponent.getChildAt(i)
                    val weightOfThis: Float = if (i == 0) {
                        rangebarView.thicknessSlider_custom.value
                    } else {
                        var diff = (rangebarView.rangeBar.values[1] - rangebarView.rangeBar.values[0])
                        if (diff == 0f) {
                            diff
                        } else {
                            diff + 1
                        }
                    }
                    val barViewLp = LinearLayout.LayoutParams(
                        0,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        weightOfThis
                    )
                    barView.orientation = LinearLayout.HORIZONTAL
                    barView.setBackgroundColor(arrayList[i].color)
                    barView.layoutParams = barViewLp
                    b.barViewLayout.addView(barView)
                }
            }
        }



    }
}