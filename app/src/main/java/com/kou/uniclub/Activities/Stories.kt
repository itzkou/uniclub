package com.kou.uniclub.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_stories.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Stories : AppCompatActivity() {
    private var ressources = arrayListOf<Int>(R.drawable.logo_bar, R.drawable.im_event)
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L
    private var listener = object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressTime = System.currentTimeMillis()
                    storyView.pause()
                    return false
                }
                MotionEvent.ACTION_UP -> {
                    val now = System.currentTimeMillis()
                    storyView.resume()
                    return limit < now - pressTime
                }
            }
            return false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_stories)

        val service=UniclubApi.create()
        service.getUpcomingEvents().enqueue(object : Callback<EventListResponse>{
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {

                if (response.isSuccessful)
                { storyView.setStoriesListener(object :StoriesProgressView.StoriesListener{
                    override fun onComplete() {
                    }

                    override fun onPrev() {

                    }

                    override fun onNext() {

                    }

                })
                    Picasso.get().load(response.body()!!.pagination.events[0].photo).into(sImage)
                    counter=1
                    storyView.setStoriesCount(counter)
                    storyView.setStoryDuration(3000L)
                    storyView.startStories()

                }
            }

        })


    }


}
