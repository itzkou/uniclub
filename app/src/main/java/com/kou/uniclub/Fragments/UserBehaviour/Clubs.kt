package com.kou.uniclub.Fragments.UserBehaviour

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import com.kou.uniclub.Adapter.RvFollowedClubsAdapter
import com.kou.uniclub.Extensions.OnBottomReachedListener
import com.kou.uniclub.Model.Club.ClubsResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Clubs : Fragment() {

    private var page: String? = null

    companion object {

        fun newInstance(): Clubs = Clubs()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_followed_clubs, container, false)
        val rvFollowedClubs = v.findViewById<RecyclerView>(R.id.rvFollowedClubs)
        val clubPH = v.findViewById<ConstraintLayout>(R.id.clubPH)
        val token = PrefsManager.geToken(activity!!)

        rvFollowedClubs.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        if (token != null) {
            val service = UniclubApi.create()
            service.getFollows("Bearer $token").enqueue(object : Callback<ClubsResponse> {
                override fun onFailure(call: Call<ClubsResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<ClubsResponse>, response: Response<ClubsResponse>) {
                    if (response.isSuccessful && isAdded) {
                        if (response.body()!!.pagination.clubs.size>0) {
                            clubPH.animation=AnimationUtils.loadAnimation(activity!!,R.anim.abc_shrink_fade_out_from_bottom)
                            clubPH.visibility = View.GONE

                        }
                        page = response.body()!!.pagination.nextPageUrl
                        val adapter = RvFollowedClubsAdapter(response.body()!!.pagination.clubs, activity!!)
                        rvFollowedClubs.adapter = adapter

                        //Pagination
                        adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
                            override fun onBottomReached(position: Int) {
                                if (page != null)
                                    getMoreItems(adapter)
                            }

                        })


                    }
                }

            })
        } else
            clubPH.visibility = View.VISIBLE



        return v
    }

    private fun getMoreItems(adapter: RvFollowedClubsAdapter) {
        val service = UniclubApi.create()
        if (page != null)
            service.paginateClubs(page!!).enqueue(object : Callback<ClubsResponse> {
                override fun onFailure(call: Call<ClubsResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<ClubsResponse>, response: Response<ClubsResponse>) {
                    if (response.isSuccessful && isAdded) {

                        if (page != null) {
                            adapter.addData(response.body()!!.pagination.clubs)
                            page = response.body()!!.pagination.nextPageUrl
                            if (page == null)
                                Toasty.custom(
                                    activity!!,
                                    "Load more",
                                    com.kou.uniclub.R.drawable.ic_error_outline_white_24dp,
                                    com.kou.uniclub.R.color.toasty,
                                    Toasty.LENGTH_SHORT,
                                    false,
                                    true
                                ).show()


                        }

                    }
                }

            })

    }

}