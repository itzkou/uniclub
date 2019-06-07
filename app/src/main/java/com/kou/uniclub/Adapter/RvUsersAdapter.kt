package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kou.uniclub.Activities.ChatLog
import com.kou.uniclub.Model.User.UserFire
import com.kou.uniclub.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_user.view.*

class RvUsersAdapter(val users: ArrayList<UserFire>, val context: Context) :
    RecyclerView.Adapter<RvUsersAdapter.VH>() {
    companion object {
        val USER_KEY="User_key"

    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(vh: VH, position: Int) {
        val user = users[position]
        Picasso.get().load(user.pic).into(vh.photo)
        vh.username.text = user.username

        vh.root.setOnClickListener {
            val intent=Intent(context,ChatLog::class.java)
            intent.putExtra(USER_KEY,user)
            context.startActivity(intent)
        }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val username = view.tvUsername!!
        val photo = view.imUser!!
        val root=view.rootMsg!!
    }
}