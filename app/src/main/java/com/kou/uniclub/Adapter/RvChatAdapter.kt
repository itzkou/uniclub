package com.kou.uniclub.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.kou.uniclub.Adapter.RvUsersAdapter.Companion.toUserPic
import com.kou.uniclub.Model.Chat.Message
import com.kou.uniclub.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_chat_from.view.*
import kotlinx.android.synthetic.main.row_chat_to.view.*

class RvChatAdapter(val messages: ArrayList<Message>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val FROM = 1
        val TO = 2

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FROM -> chatFromVH(LayoutInflater.from(parent.context).inflate(R.layout.row_chat_from, parent, false))
            TO -> chatToVH(LayoutInflater.from(parent.context).inflate(R.layout.row_chat_to, parent, false))
            else -> throw IllegalArgumentException()
        }

    }

    override fun getItemCount(): Int {
        return messages.size

    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.fromId == FirebaseAuth.getInstance().uid  )
            FROM
        else
            TO
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when(holder.itemViewType){
            FROM->{
                (holder as chatFromVH).tvMsg.text=message.text
            }
            TO->{(holder as chatToVH).tvMsg.text=message.text
                Picasso.get().load(toUserPic).into((holder as chatToVH).imUser)}
        }
    }
    fun add(chatMsg: Message) {
        messages.add(chatMsg)
        notifyItemInserted(messages.size)
    }

    public  class chatFromVH(view: View) : RecyclerView.ViewHolder(view) {

        val tvMsg = view.tvFrom!!

    }

    public class chatToVH(view: View) : RecyclerView.ViewHolder(view) {

        val imUser = view.imTo!!
        val tvMsg = view.tvTo!!

    }


}