package com.kou.uniclub.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.kou.uniclub.Adapter.SimpleAdapter
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.R
import com.kou.uniclub.UI.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_notifications.*

class Notifications : AppCompatActivity() {
    //TODO(" Cha,ge simple adapter and do ur logic  in notifs web service")
    private val simpleAdapter = SimpleAdapter((1..5).map { "Item: $it" }.toMutableList())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        rvNotifs.layoutManager = LinearLayoutManager(this@Notifications)
        rvNotifs.adapter = simpleAdapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvNotifs.adapter as SimpleAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvNotifs)


        /**** back ***/
        back.setOnClickListener {
            startActivity(Intent(this@Notifications,Home::class.java))
        }

        /**** Settings ***/
        imSettings.setOnClickListener {
            BuilderSettings.showSettings(this@Notifications)
        }
    }
}
