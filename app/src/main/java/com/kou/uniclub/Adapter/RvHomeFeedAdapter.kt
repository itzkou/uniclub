package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.kou.uniclub.Activities.EventDetails
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Model.User.FavoriteResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.kou.uniclub.UI.ImagePreviewer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_event_feed.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class RvHomeFeedAdapter (val events :ArrayList<EventX>, val context: Context): RecyclerView.Adapter<RvHomeFeedAdapter.Holder>() {

    companion object {
        var event_id: Int? = null

    }




    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(parent.context).inflate(com.kou.uniclub.R.layout.row_event_feed, parent, false))
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val event: EventX = events[position]
            //date stuff
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(event.startTime)
        val day = DateFormat.format("dd", date) as String
        val month=DateFormat.format("MMM",date) as String


        holder.title.text=event.name
        holder.place.text=event.location
        holder.month.text=month
        holder.day.text=day
            if(!event.photo.isEmpty())
            Picasso.get().load(event.photo).into(holder.pic)
        else holder.pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.im_event))



        holder.fav.setOnClickListener {
            if(PrefsManager.geToken(context)!=null)
            { //TODO("change drawable color")
                val service=UniclubApi.create()
                service.favorite(PrefsManager.geToken(context)!!,event.id).enqueue(object: Callback<FavoriteResponse>{
                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                            Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
                            }

                    override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                        Toast.makeText(context,response.body()!!.message,Toast.LENGTH_SHORT).show()

                    }

                })
            }
            else
            {

                val dialogView = LayoutInflater.from(context).inflate(R.layout.builder_feature_access, null)
                val anim=dialogView.findViewById<LottieAnimationView>(R.id.animAuth)
                val builder = AlertDialog.Builder(context)
                builder.setView(dialogView)
                builder.setPositiveButton("confirm") { dialog, which ->

                }

                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    dialog?.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
                anim.playAnimation()

            }
        }

        //EventO details
        holder.pic.setOnClickListener {


            event_id=event.id
            Log.d("id_ev", event_id.toString())
            context.startActivity(Intent(context, EventDetails::class.java))

        }

        holder.pic.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                ImagePreviewer().show(v!!.context,holder.pic)
                return false
            }

        })

    }



    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.title!!
        val day = view.day!!
        val month = view.month!!
        val place = view.place!!
        val fav= view.favorite!!
        //TODO("don forget parsing image with picasso in On bindViewholder")
        val pic= view.im_event!!

    }



    fun addData(listItems: ArrayList<EventX>) {
        val size = this.events.size
        val sizeNew = listItems.size
        Log.d("sizespoo",events.size.toString())

        if (size<sizeNew+size) {
            this.events.addAll(listItems)
            notifyItemRangeInserted(size, sizeNew)
        }

    }


}
