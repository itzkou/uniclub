package com.kou.uniclub.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.kou.uniclub.Adapter.RvNotifsAdapter
import com.kou.uniclub.Model.Notification.Notif
import com.kou.uniclub.Model.Notification.NotificationResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.kou.uniclub.UI.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_notifications.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Notifications : AppCompatActivity() {


    // private val simpleAdapter = RvNotifsAdapter((1..5).map { "Item: $it" }.toMutableList())
    private var simpleAdapter= RvNotifsAdapter(arrayListOf<Notif>(),this@Notifications)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        if (PrefsManager.geToken(this@Notifications) != null)
            getNotifs(rvNotifs)

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvNotifs.adapter as RvNotifsAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvNotifs)


        /**** back ***/
        back.setOnClickListener {
            startActivity(Intent(this@Notifications, Home::class.java))
        }

        /**** Settings ***/
        imCancel.setOnClickListener {
            simpleAdapter.deselectAll()
            imCancel.visibility = View.INVISIBLE


        }

        imSelect.setOnClickListener {
            simpleAdapter.selectAll()
            imCancel.visibility = View.VISIBLE
        }

        imDelete.setOnClickListener {
            simpleAdapter.removeAll()
        }


    }

    private fun getNotifs(rv: RecyclerView) {
        val service = UniclubApi.create()
        service.getNotifs("Bearer " + PrefsManager.geToken(this@Notifications))
            .enqueue(object : Callback<NotificationResponse> {
                override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                    if (response.isSuccessful) {
                        simpleAdapter = RvNotifsAdapter(response.body()!!.notifs, this@Notifications)
                        rv.adapter = simpleAdapter

                    }
                }

            })
    }
}
