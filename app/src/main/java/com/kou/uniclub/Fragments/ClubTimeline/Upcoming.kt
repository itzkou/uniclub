package com.kou.uniclub.Fragments.ClubTimeline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.R


class Upcoming:Fragment() {

    companion object {

        fun newInstance(): Upcoming = Upcoming()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(R.layout.fragment_upcoming,container,false)
        return v    }


}