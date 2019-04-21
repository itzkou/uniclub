package com.kou.uniclub.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.Html
import android.widget.TextView
import com.kou.uniclub.Adapter.VpSliderAdapter
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_intro.*

class Intro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        val sliderAdapter= VpSliderAdapter(this@Intro)
      slider_pager.adapter=sliderAdapter
        addDots(0)

       slider_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                addDots(position)

                if(position==2) {
                    skip.setOnClickListener {
                        //TODO("Where auth ocurs")
                        //startActivity(Intent(this@Intro, Auth::class.java))
                        //finish()
                    }
                }

                //TODO("change this logic later")
                    /*
                //on last page selected wait 2 sec and launch activity
                if (position==2 && !SaveSharedPreference.getLoggedStatus(this@Intro)) {
                    skip.visibility= View.VISIBLE
                    skip.setOnClickListener {
                        startActivity(Intent(this@Intro, SignUp::class.java))
                    }
                 */
            }

        })

    }

    private fun addDots( position:Int){
        val Dots= arrayOfNulls<TextView>(3)
        //remove all dots khater kol viewpager scrooll yzid dots 3 fois
        dots_layout.removeAllViews()

        for(i in Dots.indices)
        { Dots[i]= TextView(this@Intro)
            Dots[i]!!.text= Html.fromHtml("&#8226")

            Dots[i]!!.textSize= 41F
            dots_layout.addView(Dots[i])
            Dots[i]!!.setTextColor(ContextCompat.getColor(this, R.color.transparent))



        }
        when (position) {

            0 -> {  Dots[position]!!.setTextColor(ContextCompat.getColor(this, R.color.black))

            }
            1 -> {
                Dots[position]!!.setTextColor(ContextCompat.getColor(this, R.color.black))

            }
            2 ->   {Dots[position]!!.setTextColor(ContextCompat.getColor(this, R.color.black))

            }
        }
    }
}
