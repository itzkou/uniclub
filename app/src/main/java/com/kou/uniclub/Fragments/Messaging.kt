package com.kou.uniclub.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kou.uniclub.Activities.Notifications
import com.kou.uniclub.Adapter.RvUsersAdapter
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Extensions.BuilderSettings
import com.kou.uniclub.Model.User.UserFire
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager

class Messaging:Fragment() {

    companion object {

        fun newInstance():Messaging=Messaging()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_messaging,container,false)
        val rvMsg=v.findViewById<RecyclerView>(R.id.rvUsers)
        val imNotifs = v.findViewById<ImageView>(R.id.imNotifs)
        val imSettings = v.findViewById<ImageView>(R.id.imSettings)
        val token = PrefsManager.geToken(activity!!)
        val fab = v.findViewById<FloatingActionButton>(R.id.fabSearch)

        if (PrefsManager.geToken(activity!!) != null && FirebaseAuth.getInstance().uid != null)
        fetchUsers(rvMsg)

        /********** Notifications  ****************/
        if (token != null)
            imNotifs.setOnClickListener {
                startActivity(Intent(activity!!, Notifications::class.java))
            }

        /********Settings ******/
        imSettings.setOnClickListener {
            if (token != null)
                BuilderSettings.showSettings(activity!!)
            else
                BuilderAuth.showDialog(activity!!)
        }


        return v
    }


    private fun fetchUsers(rv:RecyclerView)
    {
        val ref=FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }


            override fun onDataChange(p0: DataSnapshot) {
                val users=ArrayList<UserFire>()
                p0.children.forEach {
                    val user=it.getValue(UserFire::class.java)
                    if (user!=null&&user.uid!=FirebaseAuth.getInstance().uid)
                        users.add(user)
                }
                if (isAdded)
                rv.adapter=RvUsersAdapter(users,activity!!)

            }

        })
    }
}