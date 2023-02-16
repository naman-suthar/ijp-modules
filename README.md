# Modules
Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

		dependencies {  implementation 'com.github.naman-suthar:Modules:Version' }
 ### For Color Dialog
  
 	 button.setOnClickListener { view ->
            val colorDialog = ColorDialog.newInstance(
                0,   //Old Color or previously selected Color
                object : OnColorChangedListener{
                    override fun colorChanged(color: Int) {
                        //*change oldColor = color for next selection iteration*
                        //*Do whatever you want to perform task with selected Color*
                        
                    }
                }
            )

            colorDialog?.show(supportFragmentManager, "Main")
        }
        
       
  ### For Segments Fragment
 
 	SegmentColorFragment() {updatedlist->
            *perform your tasks here after geting updated list i.e. save it to db
        }.apply {
            this.updateList(
                initialList  //provide initial list or by default it has empty List
            )
        }

 	The Segment Colors Rangebar list looks like
                    
	val initialList = mutableListOf(
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
	

