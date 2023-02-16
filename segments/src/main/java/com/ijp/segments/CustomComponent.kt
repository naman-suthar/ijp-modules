package com.ijp.segments

import android.app.ActionBar
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.ijp.colorpicker.ColorDialog
import com.ijp.colorpicker.OnColorChangedListener
import com.ijp.segments.model.RangeBarArray
import kotlinx.android.synthetic.main.each_rangebar.view.*


var defaultColor: Int = 0

class CustomComponent @JvmOverloads constructor(
    context: Context,
    callback: CustomComponentsCallback,
    supportFragmentManager: FragmentManager,
    liveBarParentLayout:LinearLayout,
    parentLay: LinearLayout,
    arrayList: MutableList<RangeBarArray>,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) :
    ConstraintLayout(context, attrs, defStyle, defStyleRes) {


    init {

        fun barLiveView() {
            //live Bar change logic
            liveBarParentLayout.removeAllViews()        //removes all views
            for (i in 0 until arrayList.size) {     //creates linearlayout of weights of RangeSlider
                val barView = LinearLayout(context)
                val rangebarView = parentLay.getChildAt(i)
                var weightOfThis: Float = if (i == 0) {
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
                liveBarParentLayout.addView(barView)
            }
        }
        //inflating each_seekbar
        LayoutInflater.from(context).inflate(R.layout.each_rangebar, this, true)


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.CustomComponent, 0, 0
            )

            val start =
                typedArray.getInt(
                    R.styleable.CustomComponent_startText, 10
                )

            val end = typedArray.getInt(
                R.styleable.CustomComponent_endText, 90
            )


            typedArray.recycle()
            setStartText(start)
            setEndText(end)
            setProgress(start, end)

        }

        defaultColor = ContextCompat.getColor(context, R.color.backgroundColor);

        colorPickerBtn.setOnClickListener {

            //Demo old color picker invoked
            /*val colorPicker =
                AmbilWarnaDialog(context, defaultColor, object : OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog) {}
                    override fun onOk(dialog: AmbilWarnaDialog, color: Int) {

                        val note = parentLay.indexOfChild(this@CustomComponent)

                        defaultColor = color

                        arrayList[note].color = defaultColor

                        setColor(defaultColor)
                        barLiveView()
                        callback.onValueChanged()

                    }
                })
            colorPicker.show()*/

            val colorDialog =
                ColorDialog.newInstance(currentColor, object : OnColorChangedListener {
                    override fun colorChanged(color: Int) {
                        val note = parentLay?.indexOfChild(this@CustomComponent)
                        defaultColor = color
                        arrayList[note!!].color = defaultColor
                        setColor(defaultColor)
                        barLiveView()
                        callback.onValueChanged()
                    }
                })
            colorDialog.show(supportFragmentManager, context.getString(R.string.single_tag))

            // TODO ("Attention")
            /*Code to invoke Multi Color Dialog for Gradient Color Configuration's 'Feeling Lucky!' option
            newInstance requires to specify the number of colors currently present for Gradient Color Configuration (it can't be less than 2,) the UI will not delete colors if it's less than 2
             */
//            MultiColorDialog.newInstance(4).show(supportFragmentManager, "multi")
        }


        //variable to hold end of current seekbar because it keeps changing on Progress Change
        var endOfThis = getEndText()
        var startOfThis = getStartText()
        var currentGrabbedComponent = -1
        rangeBar.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {


            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
                //From these We get 3 things EndOfThis, StartOfThis and Index OF currrent Grabbed

                //initializing variables

               /* endOfThis = getEndText()
                startOfThis = getStartText()*/
                /*     endOfThis = slider.values[1].toInt()
                     startOfThis = slider.values[0].toInt()*/
                currentGrabbedComponent = parentLay.indexOfChild(this@CustomComponent)
                startOfThis = arrayList[currentGrabbedComponent].start
                endOfThis = arrayList[currentGrabbedComponent].end


            }

            //On Stop Is working Perfectly
            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped

                val values = slider.values
                var left = values[0].toInt()
                var right = values[1].toInt()
                var note = parentLay.indexOfChild(this@CustomComponent)
                var sze = arrayList.size

                //If both ends are equal then delete it
                if (left == right) {

                    //delete the current node
                    if (left != startOfThis || note == sze - 1) {
                        arrayList[note - 1].end = endOfThis
                        arrayList.removeAt(note)
                    } else {
                        arrayList[note + 1].start = startOfThis
                        arrayList.removeAt(note)
                    }


                } else {

                    //If left is not equal to what it was on start, then it has moved
                    if (left != startOfThis) {

                        if (startOfThis != 0) {
                            //It it is not the first bar, (it's starting is not 0)
                            var deletionNote = 0
                            for (i in 0 until arrayList.size) {
                                if (left >= arrayList[i].start && left <= arrayList[i].end) {

                                    deletionNote = i

                                    break
                                }
                            }
                            if (deletionNote == note) {
                                arrayList[note - 1].end = left - 1
                                arrayList[note].start = left
                                arrayList[note].end = endOfThis
                            } else {
                                if (left == 0) {
                                    arrayList[note].start = 0
                                    while (note != 0) {
                                        arrayList.removeAt(0)
                                        note--
                                    }
                                } else {
                                    arrayList[deletionNote].end = left - 1
                                    arrayList[note].start = left

                                    deletionNote++
                                    while (deletionNote < note) {
                                        arrayList.removeAt(deletionNote)
                                        note--
                                    }
                                }
                            }
                        }

                    } else {

                        if (endOfThis == 100 || note == sze - 1) {
                            //If its starting is zero but there is only onr bar
                            if (right - left < 6 || 100 - right < 6) {
                                setStartText(startOfThis)
                                setProgress(startOfThis, 100)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.min_text2),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {


                                val rangeBar1 = RangeBarArray(
                                    right + 1,
                                    100
                                )
                                arrayList[note].end = right
                                arrayList.add(note + 1, rangeBar1)

                            }
                        } else {
                            //If is the first bar and there are more than one rangeBars
                            var deletionNote = note
                            for (i in 0 until arrayList.size) {
                                if (right >= arrayList[i].start && right <= arrayList[i].end) {

                                    deletionNote = i

                                    break
                                }
                            }
                            if (deletionNote == note) {
                                arrayList[note + 1].start = right + 1
                                arrayList[note].start = startOfThis
                                arrayList[note].end = right
                            } else {
                                if (right == 100) {
                                    arrayList[note].end = 100
                                    while (note != sze - 1) {
                                        arrayList.removeAt(sze - 1)
                                        sze--
                                    }
                                } else {
                                    arrayList[deletionNote].start = right + 1
                                    arrayList[note].end = right

                                    deletionNote--
                                    while (deletionNote > note) {
                                        arrayList.removeAt(deletionNote)
                                        deletionNote--
                                    }
                                }
                            }


                        }
                    }

                }


                //Refreshing logic
                parentLay.removeAllViews()
                val lp = ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )

                //Re-apply the whole list
                for (i in arrayList) {


                    val newCompo = CustomComponent(context, callback, supportFragmentManager,liveBarParentLayout,parentLay,arrayList)
                    newCompo.setStartText(i.start)
                    newCompo.setEndText(i.end)
                    newCompo.setProgress(i.start, i.end)
                    if (i.start == 0) {
                        newCompo.thicknessSlider_custom.visibility = View.VISIBLE
                        newCompo.rangeBar.visibility = View.INVISIBLE
//                        newCompo.setFirstColor(i.color)
//
                    }
                    newCompo.setColor(i.color)


                    parentLay.addView(newCompo, lp)


                }

                barLiveView()
                callback.onValueChanged()
            }
        })

        rangeBar.addOnChangeListener { rangeSlider, value, fromUser ->
            // Responds to when slider's value is changed

            val values = rangeSlider.values
            val left = values[0].toInt()
            val right = values[1].toInt()

            //to change the endtext
            setStartText(left)
            setEndText(right)

            //to make the delete image  Visible
            if (values[0] == values[1]) {
                leftImage.visibility = View.VISIBLE
                rightImage.visibility = View.VISIBLE
            }
            if (values[0] != values[1]) {
                rightImage.visibility = View.GONE
                leftImage.visibility = View.GONE
            }


            var note = -1
            for (i in 0 until arrayList.size) {

                if (
                    startOfThis >= arrayList[i].start && endOfThis <= arrayList[i].end
                ) {
                    note = i
                    break
                }

            }

            val sze = arrayList.size

            if (left != startOfThis) {
                if (note <= 0) {

                    if (note == 0) {
                        rangeSlider.setValues(0f, arrayList[0].end.toFloat())


                    } else {
                        //There is some error with note
                    }

                } else {
                    var i = 1

                    if (left != 0) {
                        while (left < arrayList[note - i].start) {
                            val temp = parentLay.getChildAt(note - i)

                            temp.rangeBar.setValues(
                                arrayList[note - i].start.toFloat(),
                                arrayList[note - i].start.toFloat()
                            )
                            temp.rangeBar.visibility = View.INVISIBLE
                            temp.thicknessSlider_custom.visibility = View.INVISIBLE
                            i++
                        }
                        var j = 0
                        while (left > arrayList[j].start) {
                            val temp = parentLay.getChildAt(j)

                            temp.rangeBar.visibility = View.VISIBLE
                            if (j == 0) {
                                temp.rangeBar.visibility = View.INVISIBLE
                                temp.thicknessSlider_custom.visibility = View.VISIBLE
                            }

                            j++
                            if (j == sze) {
                                break
                            }

                        }
                        var k = 0
                        while (left > arrayList[k].end) {
                            val temp = parentLay.getChildAt(k)
                            temp.rangeBar.setValues(
                                arrayList[k].start.toFloat(),
                                arrayList[k].end.toFloat()
                            )
                            if (k == 0) {
                                temp.thicknessSlider_custom.value = arrayList[k].end.toFloat()
                            }
                            k++
                        }

                        val previousComponent = parentLay.getChildAt(note - i)
                        val startOfPrevious = arrayList[note - i].start.toFloat()
                        val endOfPrevious =
                            (left).toFloat()
                        previousComponent.rangeBar.setValues(startOfPrevious, endOfPrevious)
                        previousComponent.thicknessSlider_custom.value = endOfPrevious
                    } else {
                        val temp = parentLay.getChildAt(0)

                        temp.rangeBar.setValues(left.toFloat(), left.toFloat())
                        temp.rangeBar.visibility = View.INVISIBLE
                        temp.thicknessSlider_custom.visibility = View.INVISIBLE

                    }
                    barLiveView()
                }
            }
            if (currentGrabbedComponent != -1) {
                if (right != endOfThis && note == currentGrabbedComponent) {
                    if (note >= sze - 1) {

                        if (note == sze - 1) {

                            barLiveView()


                            //Live bar View logic
                            val barView = LinearLayout(context)
                            val rangebarView = parentLay.getChildAt(note)
                            var weightOfThis: Float = 100 - rangebarView.rangeBar.values[1]
                            val barViewLp = LinearLayout.LayoutParams(
                                0,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                weightOfThis
                            )
                            barView.orientation = LinearLayout.HORIZONTAL
                            barView.setBackgroundColor(Color.parseColor("#2D9EDEFA"))
                            barView.layoutParams = barViewLp
                            liveBarParentLayout.addView(barView)


                        } else {
                            //There is some error with note
                        }

                    } else {
                        var i = 1
                        if (right != 100) {
                            while (right > arrayList[note + i].end) {
                                val temp = parentLay.getChildAt(note + i)

                                temp?.rangeBar?.setValues(
                                    arrayList[note + i].end.toFloat(),
                                    arrayList[note + i].end.toFloat()
                                )
                                temp?.rangeBar?.visibility = View.INVISIBLE

                                i++
                            }
                            var j = sze - 1
                            while (right < arrayList[j].end) {
                                val temp = parentLay.getChildAt(j)

                                temp.rangeBar.visibility = View.VISIBLE
                                j--
                                if (j == 0) {
                                    break
                                }

                            }
                            var k = sze - 1
                            while (right < arrayList[k].start) {
                                val temp = parentLay.getChildAt(k)
                                temp.rangeBar.setValues(
                                    arrayList[k].start.toFloat(),
                                    arrayList[k].end.toFloat()
                                )
                                k--
                            }
                            val nextComponent = parentLay.getChildAt(note + i)
                            val startOfNext = right.toFloat()
                            val endOfNext =
                                (arrayList[note + i].end).toFloat()
                            nextComponent?.rangeBar?.setValues(startOfNext, endOfNext)
                        } else {
                            val temp = parentLay.getChildAt(sze - 1)

                            temp.rangeBar.setValues(right.toFloat(), right.toFloat())
                            temp.rangeBar.visibility = View.INVISIBLE


                        }
                        barLiveView()
                    }
                }
            }


        }

        //From here is the logic for single Thumb bar that is only visible on first bar.

        thicknessSlider_custom.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started

                //initializing variables
                /*    endOfThis = getEndText()
                    startOfThis = getStartText()*/
                startOfThis = 0
                endOfThis = slider.value.toInt()
                currentGrabbedComponent = parentLay.indexOfChild(this@CustomComponent)
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped

                val note = parentLay.indexOfChild(this@CustomComponent)
                val end = arrayList[note].end
                val left = 0
                val right = slider.value.toInt()
                var sze = arrayList.size
                if (left == right) {

                    if (note == 0) {
                        //If it is first  then delete it
                        arrayList[1].start = 0
                        arrayList.removeAt(0)
                    } else {
                        //if it is not first then delete current one only
                        arrayList[note - 1].end = endOfThis
                        arrayList.removeAt(note)
                    }

                } else {
                    if (end == 100) {

                        if (right - left < 6 || end - right < 6) {
                            setStartText(0)
                            setProgress(0, end)
                            Toast.makeText(
                                context,
                                context.getString(R.string.min_text3),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {


                            val rangeBar1 = RangeBarArray(
                                right + 1,
                                end
                            )
                            arrayList[note].end = right
                            arrayList.add(note + 1, rangeBar1)

                        }
                    } else {

                        var deletionNote = note
                        for (i in 0 until arrayList.size) {
                            if (right >= arrayList[i].start && right <= arrayList[i].end) {

                                deletionNote = i

                                break
                            }
                        }
                        if (deletionNote == note) {
                            arrayList[note + 1].start = right + 1
                            arrayList[note].start = 0
                            arrayList[note].end = right
                        } else {
                            if (right == 100) {
                                arrayList[note].end = 100
                                while (note != sze - 1) {
                                    arrayList.removeAt(sze - 1)
                                    sze--
                                }
                            } else {
                                arrayList[deletionNote].start = right + 1
                                arrayList[note].end = right

                                deletionNote--
                                while (deletionNote > note) {
                                    arrayList.removeAt(deletionNote)
                                    deletionNote--
                                }
                            }
                        }
                    }
                }

                //Refreshing logic
                parentLay.removeAllViews()
                val lp = ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )

                //Re-apply the whole list
                for (i in arrayList) {


                    val newCompo = CustomComponent(context, callback, supportFragmentManager,liveBarParentLayout,parentLay,arrayList)
                    newCompo.setStartText(i.start)
                    newCompo.setEndText(i.end)
                    newCompo.setProgress(i.start, i.end)
                    if (i.start == 0) {
                        newCompo.thicknessSlider_custom.visibility = View.VISIBLE
                        newCompo.rangeBar.visibility = View.INVISIBLE
                    }
                    newCompo.setColor(i.color)


                    parentLay.addView(newCompo, lp)


                }


                barLiveView()
                callback.onValueChanged()


            }
        })

        thicknessSlider_custom.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            val right = slider.value.toInt()


            //to change the endtext
//            setStartText(0)
            setEndText(right)

            //to make the delete image  Visible
            if (currentGrabbedComponent == 0) {
                if (right == 0) {
                    leftImage.visibility = View.VISIBLE
                    rightImage.visibility = View.VISIBLE
                }
                if (right != 0) {
                    rightImage.visibility = View.GONE
                    leftImage.visibility = View.GONE
                }
            }


            val note = 0

            val sze = arrayList.size

            if (currentGrabbedComponent != -1) {
                if (right != endOfThis && note == currentGrabbedComponent) {


                    if (note >= sze - 1) {

                        if (note == sze - 1) {
                            //Live Bar View Logic when new rangebar is to be created
                            liveBarParentLayout.removeAllViews()
                            val barView1 = LinearLayout(context)
                            val barView2 = LinearLayout(context)
                            val rangebarView = parentLay.getChildAt(0)
                            val barViewLp1 = LinearLayout.LayoutParams(
                                0,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                rangebarView.thicknessSlider_custom.value
                            )
                            val barViewLp2 = LinearLayout.LayoutParams(
                                0,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                100 - rangebarView.thicknessSlider_custom.value
                            )
                            barView1.orientation = LinearLayout.HORIZONTAL
                            barView2.orientation = LinearLayout.HORIZONTAL

                            barView1.setBackgroundColor(arrayList[0].color)
                            barView2.setBackgroundColor(Color.parseColor("#2D9EDEFA"))

                            barView1.layoutParams = barViewLp1
                            barView2.layoutParams = barViewLp2
                            liveBarParentLayout.addView(barView1)
                            liveBarParentLayout.addView(barView2)

                        } else {
                            //There is some error with note
                        }

                    } else {
                        var i = 1

                        if (right != 100) {
                            while (right > arrayList[note + i].end) {
                                val temp = parentLay.getChildAt(note + i)

                                temp?.rangeBar?.setValues(
                                    arrayList[note + i].end.toFloat(),
                                    arrayList[note + i].end.toFloat()
                                )
                                temp?.rangeBar?.visibility = View.INVISIBLE

                                i++
                            }
                            var j = sze - 1
                            while (right < arrayList[j].end) {
                                val temp = parentLay.getChildAt(j)

                                temp?.rangeBar?.visibility = View.VISIBLE
                                j--
                                if (j == 0) {
                                    break
                                }

                            }
                            var k = sze - 1
                            while (right < arrayList[k].start) {
                                val temp = parentLay.getChildAt(k)
                                temp?.rangeBar?.setValues(
                                    arrayList[k].start.toFloat(),
                                    arrayList[k].end.toFloat()
                                )
                                k--
                            }
                            val nextComponent = parentLay.getChildAt(note + i)
                            val startOfNext = right.toFloat()
                            val endOfNext =
                                (arrayList[note + i].end).toFloat()
                            nextComponent?.rangeBar?.setValues(startOfNext, endOfNext)
                        } else {
                            val temp = parentLay.getChildAt(sze - 1)

                            temp.rangeBar.setValues(right.toFloat(), right.toFloat())
                            temp.rangeBar.visibility = View.INVISIBLE


                        }


                        barLiveView()

                    }
                }
            }
        }


    }


    fun getStartText(): Int {
        return Integer.parseInt(startTxt!!.text.toString())

    }

    fun setStartText(value: Int) {
        startTxt!!.text = value.toString()
    }

    fun getEndText(): Int {
        return Integer.parseInt(endTxt!!.text.toString())
    }

    fun setEndText(value: Int) {
        endTxt!!.text = value.toString()
    }

    fun setProgress(left: Int, right: Int) {
        rangeBar.valueFrom = 0f
        rangeBar.valueTo = 100f

        rangeBar.setValues(left.toFloat(), right.toFloat())
        thicknessSlider_custom.value = right.toFloat()
    }

    var currentColor = 0

    fun setColor(color: Int) {
        val clr = color
        currentColor = color
        ImageViewCompat.setImageTintList(colorPickerBtn, ColorStateList.valueOf(clr))


        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled)
        )

        val colors = intArrayOf(
            clr
        )

        val myList = ColorStateList(states, colors)
        rangeBar.trackActiveTintList = myList
        rangeBar.thumbTintList = myList

        thicknessSlider_custom.trackActiveTintList = myList
        thicknessSlider_custom.thumbTintList = myList


    }


    //Function to update the LIVE VIEW BAR


}