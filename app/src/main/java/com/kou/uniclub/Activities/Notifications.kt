package com.kou.uniclub.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.kou.uniclub.Adapter.NotifsAdapter
import com.kou.uniclub.Model.Notif.Notification
import com.kou.uniclub.R
import com.kou.uniclub.UI.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_notifications.*

class Notifications : AppCompatActivity() {
    private val n1 = Notification("Hello I am Notif one")
    private val n2 = Notification("Hello I am Notif two")

    // private val simpleAdapter = NotifsAdapter((1..5).map { "Item: $it" }.toMutableList())
    private val simpleAdapter = NotifsAdapter(arrayListOf(n1, n2), this@Notifications)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        rvNotifs.layoutManager = LinearLayoutManager(this@Notifications)
        rvNotifs.adapter = simpleAdapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvNotifs.adapter as NotifsAdapter
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
}
