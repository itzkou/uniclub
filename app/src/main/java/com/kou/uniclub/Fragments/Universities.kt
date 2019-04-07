package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.kou.uniclub.Adapter.UnivsAdapter
import com.kou.uniclub.Model.UniversityResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import kotlinx.android.synthetic.main.fragment_clubs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class Universities: Fragment() {

    companion object {

        fun newInstance():Universities = Universities()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_clubs,container,false)

        //fetching universities
        val uniclub=UniclubApi.create()
        uniclub.getUniversities().enqueue(object: Callback<UniversityResponse>{
            override fun onFailure(call: Call<UniversityResponse>, t: Throwable) {
                if(t  is IOException)
                    Toast.makeText(activity!!,"Network faillure Univerisities",Toast.LENGTH_SHORT).show()
            else    Toast.makeText(activity!!,"conversion error",Toast.LENGTH_SHORT).show()}

            override fun onResponse(call: Call<UniversityResponse>, response: Response<UniversityResponse>) {
                if(response.isSuccessful) {
                    rvUnivs.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
                    rvUnivs.adapter = UnivsAdapter(response.body()!!.data, activity!!)
                }
            }

        })

        return v
    }

}