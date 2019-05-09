package com.kou.uniclub.Fragments.UserBehaviour

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvFavoEventsAdapter
import com.kou.uniclub.Model.User.MyfavoritesResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Events : Fragment() {
    companion object {

        fun newInstance(): Events = Events()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_favorite_events, container, false)
        val rvFavo = v.findViewById<RecyclerView>(R.id.rvFavoEvents)

        rvFavo.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
            if (PrefsManager.geToken(activity!!)!=null) {
                val service = UniclubApi.create()
                service.getFavorites("Bearer " + PrefsManager.geToken(activity!!)!!)
                    .enqueue(object : Callback<MyfavoritesResponse> {
                        override fun onFailure(call: Call<MyfavoritesResponse>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<MyfavoritesResponse>,
                            response: Response<MyfavoritesResponse>
                        ) {
                            if (response.isSuccessful&&isAdded) {
                                rvFavo.adapter = RvFavoEventsAdapter(response.body()!!.events, activity!!)
                            }
                        }

                    })
            }
        return v
    }


}