package com.kou.uniclub.Fragments.ClubTimeline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.R


class Sponsors:Fragment() {

    companion object {

        fun newInstance(): Sponsors = Sponsors()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(R.layout.fragment_sponsors,container,false)
        return v
    }


}