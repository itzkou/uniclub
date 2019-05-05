package com.kou.uniclub.Fragments.UserBehaviour

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R

class Clubs: Fragment() {
    companion object {

        fun newInstance():Clubs=Clubs()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_followed_clubs,container,false)
        val rvFollowedClubs=v.findViewById<RecyclerView>(R.id.rvFollowedClubs)
        val service= UniclubApi.create()



        return v
    }



}