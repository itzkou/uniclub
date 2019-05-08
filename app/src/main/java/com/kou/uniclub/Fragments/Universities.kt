package com.kou.uniclub.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvUnivsAdapter
import com.kou.uniclub.Model.University.UniversityResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Universities: Fragment() {

    companion object {

        fun newInstance():Universities = Universities()
    }
//TODO("u fudging dumb findview by id all recycler views")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_univs,container,false)
        val rvUnivs=v.findViewById<RecyclerView>(R.id.rvUnivs)
            rvUnivs.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)


        val uniclub=UniclubApi.create()
        uniclub.getUniversities().enqueue(object: Callback<UniversityResponse>{
            override fun onFailure(call: Call<UniversityResponse>, t: Throwable) {
             }

            override fun onResponse(call: Call<UniversityResponse>, response: Response<UniversityResponse>) {
                if(response.isSuccessful) {
                    //TODO(" causes null pointer exception")

                    rvUnivs.adapter = RvUnivsAdapter(response.body()!!.pagination.universities, activity!!)
                }            }


        })

        return v
    }

}