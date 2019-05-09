package com.kou.uniclub.Adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kou.uniclub.Activities.EventDetails
import com.kou.uniclub.Extensions.BuilderAuth
import com.kou.uniclub.Model.Event.EventX
import com.kou.uniclub.Model.User.FavoriteResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.kou.uniclub.UI.ImagePreviewer
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.row_event_feed.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RvHomeFeedAdapter(val events: ArrayList<EventX>, val context: Context) :
    RecyclerView.Adapter<RvHomeFeedAdapter.Holder>() {

    companion object {
        var event_id: Int? = null

    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {

        return Holder(
            LayoutInflater.from(parent.context).inflate(
                com.kou.uniclub.R.layout.row_event_feed,
                parent,
                false
            )
        )
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
        val month = DateFormat.format("MMM", date) as String

        holder.title.text = event.name
        holder.place.text = event.location
        holder.month.text = month
        holder.day.text = day
        if (!event.photo.isEmpty())
            Picasso.get().load(event.photo).into(holder.pic)
        else holder.pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.im_event))


        var isLiked = false
        holder.fav.setOnClickListener {
            if (PrefsManager.geToken(context) != null) {

                if (!isLiked) {
                    val service = UniclubApi.create()
                    service.favorite("Bearer " +PrefsManager.geToken(context)!!, event.id)
                        .enqueue(object : Callback<FavoriteResponse> {
                            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                                if (t is IOException)
                                    Toasty.warning(context, "Network faillure", Toast.LENGTH_SHORT, true).show()
                            }

                            override fun onResponse(
                                call: Call<FavoriteResponse>,
                                response: Response<FavoriteResponse>
                            ) {
                                isLiked = true
                                holder.sparkle.playAnimation()
                                holder.fav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorito))
                            }

                        })
                } else if (isLiked) {
                    val service = UniclubApi.create()
                    service.unfavorite("Bearer " + PrefsManager.geToken(context)!!, event.id)
                        .enqueue(object : Callback<FavoriteResponse> {
                            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                                Toasty.warning(context, "Network faillure", Toast.LENGTH_SHORT, true).show()
                            }

                            override fun onResponse(
                                call: Call<FavoriteResponse>,
                                response: Response<FavoriteResponse>
                            ) {
                                if (response.isSuccessful) {
                                    isLiked = false
                                    holder.fav.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_favoriteg
                                        )
                                    )
                                }
                            }

                        })

                }
            } else {
                BuilderAuth.showDialog(context )

            }
        }

        //EventO details
        holder.pic.setOnClickListener {


            event_id = event.id
            Log.d("id_ev", event_id.toString())
            context.startActivity(Intent(context, EventDetails::class.java))

        }

        holder.pic.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                ImagePreviewer().show(v!!.context, holder.pic)
                return false
            }

        })

    }


    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.title!!
        val day = view.day!!
        val month = view.month!!
        val place = view.place!!
        val fav = view.favorite!!
        val pic = view.im_event!!
        val sparkle = view.sparkle!!

    }


    fun addData(listItems: ArrayList<EventX>) {
        val size = this.events.size
        val sizeNew = listItems.size

        if (size < sizeNew + size) {
            this.events.addAll(listItems)
            notifyItemRangeInserted(size, sizeNew)
        }

    }


}
