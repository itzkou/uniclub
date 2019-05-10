package com.kou.uniclub.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
    private var ressources = arrayListOf<String>()
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

        reverse.setOnClickListener {
            storyView.reverse()
        }
        reverse.setOnTouchListener(listener)

        skip.setOnClickListener {
            storyView.skip()
        }
       skip.setOnTouchListener(listener)

        val service = UniclubApi.create()
        service.getUpcomingEvents().enqueue(object : Callback<EventListResponse> {
            override fun onFailure(call: Call<EventListResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<EventListResponse>, response: Response<EventListResponse>) {

                if (response.isSuccessful) {
                    storyView.setStoriesCount(response.body()!!.pagination.events.size)
                    storyView.setStoryDuration(3000L)
                    storyView.startStories()


                    for (i in 0 until response.body()!!.pagination.events.size)
                        ressources.add(response.body()!!.pagination.events[i].photo)

                    Picasso.get().load(ressources[counter]).into(sImage)
                    storyView.setStoriesListener(object : StoriesProgressView.StoriesListener {
                        override fun onComplete() {
                            startActivity(Intent(this@Stories,Home::class.java))
                        }

                        override fun onPrev() {
                            if (counter - 1 < 0)
                                return
                            Picasso.get().load(ressources[--counter]).into(sImage)

                        }

                        override fun onNext() {
                            Picasso.get().load(ressources[++counter]).into(sImage)

                        }

                    })


                }
            }

        })


    }

    override fun onDestroy() {
        storyView.destroy()

        super.onDestroy()
    }


}
