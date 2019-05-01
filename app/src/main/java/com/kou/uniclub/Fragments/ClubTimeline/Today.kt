package com.kou.uniclub.Fragments.ClubTimeline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.R


class Today:Fragment() {

    companion object {

        fun newInstance(): Today = Today()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(R.layout.fragment_today,container,false)
        /*val service= UniclubApi.create()

        service.getEvenToday().enqueue(object: Callback<FeedResponse> {
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Toast.makeText(activity!!,"There are no events Today",Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if(response.isSuccessful)
                {
                    rvToday.layoutManager= LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
                    rvToday.adapter= RvHomeFeedAdapter(response.body()!!.data,activity!!)

                }
            }

        })*/
        return v    }


}