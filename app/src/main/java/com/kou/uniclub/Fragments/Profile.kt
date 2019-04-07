package com.kou.uniclub.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kou.uniclub.Authentication.Auth
import com.kou.uniclub.Model.UserResponse
import com.kou.uniclub.Network.UniclubApi
import com.kou.uniclub.R
import com.kou.uniclub.SharedUtils.PrefsManager
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

            Profile()


        return v
    }

    fun Profile(){
        val service=UniclubApi.create()
        service.getUser("Bearer "+PrefsManager.geToken(activity!!)).enqueue(object: Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful){
                val user=response.body()!!.user
                tv_email.text=user.email      }      }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                if(t  is IOException)
                    Toast.makeText(activity!!,"Network faillure ", Toast.LENGTH_SHORT).show()
                else   Toast.makeText(activity!!,"Conversion error", Toast.LENGTH_SHORT).show()
                Log.d("stoken",PrefsManager.geToken(activity!!).toString())
            }

        })



        }

    }
