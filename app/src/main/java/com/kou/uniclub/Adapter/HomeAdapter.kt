package com.kou.uniclub.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class HomeAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm)  {
    private val mFragmentList=  ArrayList<Fragment> () //This is a fragments array

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]

    }

    override fun getCount(): Int {
       return mFragmentList.size
    }
    fun addFragment(fragment:Fragment){  //This one adds fragments to the array
        mFragmentList.add(fragment)
    }
}