package com.kou.uniclub.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.kou.uniclub.Model.UserResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class Profile:Fragment() {
    companion object {

        fun newInstance():Profile=Profile()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_profile,container,false)
            val edEmail=v.findViewById<EditText>(R.id.edEmail)
            Profile()

        edEmail.isEnabled=false
        edEmail.isFocusable=false


        return v
    }

    fun Profile(){
        val service=UniclubApi.create()
        service.getUser("Bearer "+PrefsManager.geToken(activity!!)).enqueue(object: Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful){
                val user=response.body()!!.user
                edEmail.hint=user.email
                    Picasso.get().load("http://10.0.2.2:8000/"+user.image).into(im_profile)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                if(t  is IOException)
                    Toast.makeText(activity!!,"Network faillure ", Toast.LENGTH_SHORT).show()

            }

        })



        }

    }
