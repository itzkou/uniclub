package com.kou.uniclub.Adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kou.uniclub.R

class VpSliderAdapter(context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private var mcontext: Context = context


    //  Arrays used to bind data into a single layout file "slide_layout"
    var slider_image = intArrayOf(R.drawable.wiz1, R.drawable.wiz2, R.drawable.wiz3)
    var slider_title = arrayOf(
        "You’d rather create an account in order to follow your favorite events",
        "You have a search access to all events including universities and clubs",
        "You can also save your own favorite events in order to get the most recent updates."
    )

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj as ConstraintLayout
    }

    override fun getCount(): Int {
        return slider_image.size

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //IDK what this is but put it
        layoutInflater = mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.layout_slide, container, false)

        //now find slider views by id

        val txslide = view.findViewById<TextView>(R.id.txslide)
        val imslide = view.findViewById<ImageView>(R.id.imslide)

        //data binding
        txslide.text = slider_title[position]
        imslide.setImageResource(slider_image[position])

        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}