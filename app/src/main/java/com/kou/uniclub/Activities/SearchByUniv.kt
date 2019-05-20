package com.kou.uniclub.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.activity_clubs_filter.*

class SearchByUniv : AppCompatActivity() {
    private var page: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clubs_filter)


    }

    override fun onStart() {
        super.onStart()
        val service= UniclubApi.create()
        //call univs here

    }
    }

