package com.kou.uniclub.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ClubDetailsAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm)  {
    private val mFragmentList=  ArrayList<Fragment> ()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position)
        {
            0->"Passed"
            1->"Today"
            2->"Upcoming"

            else -> return "Sponsors"
        }
    }


    fun addFragment(fragment:Fragment){  //This one adds fragments to the array
        mFragmentList.add(fragment)
    }
}