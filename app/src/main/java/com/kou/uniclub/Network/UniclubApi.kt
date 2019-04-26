package com.kou.uniclub.Network

import com.kou.uniclub.Model.Auth.LoginResponse
import com.kou.uniclub.Model.Auth.SignUpResponse
import com.kou.uniclub.Model.Club.ClubsResponse
import com.kou.uniclub.Model.Event.EventListResponse
import com.kou.uniclub.Model.Event.EventResponse
import com.kou.uniclub.Model.University.UniversityResponse
import com.kou.uniclub.Model.User.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

interface UniclubApi{

        companion object Factory {    //retrofit instance  //trick hetheya Apiutils
            fun create():UniclubApi {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.1.3:8000/api/")//10.0.2.2:8000 emulator //put ipv4 adress//me192.168.1.4//orange 10.54.234.189
                    .build()
                return retrofit.create(UniclubApi::class.java)
            }
        }

            //*************************USER*******************
            @Multipart
            @POST("auth/signup")
            fun signUP(@Query("First_Name") fname:String,
                       @Query("Last_Name")lname:String,
                       @Query("Birth_Date")birth: Date,
                       @Query("Email")email:String,
                       @Query("password")pass:String,
                       @Query("password_confirmation") passC:String,
                       @Query("Adress") address:String,
                       @Part file: MultipartBody.Part?) :Call<SignUpResponse>


            @FormUrlEncoded
            @POST("auth/login")
            fun signIN( @Field("Email")email:String,
                        @Field("password")password:String):Call<LoginResponse>

            @GET("auth/user")
            fun getUser(@Header("Authorization") authToken:String?):Call<User>

            //*************************Events*******************
            @GET("Event/List")
            fun getEventFeed():Call<EventListResponse>

            @GET("EventDetails/{id}")
            fun getEvent(@Path("id")id:Int):Call<EventResponse>

            @GET("Event/Upcoming")
            fun getUpcomingEvents():Call<EventListResponse>



            //*************************University*******************

                    @GET("University/List ")
                    fun getUniversities():retrofit2.Call<UniversityResponse>
            //*************************Clubs*******************

                    @GET("Club/showByUniversity/{univ_id}")
                    fun getClubsByUniv(@Path("univ_id") id:Int): Call<ClubsResponse>

            //*************************Pagination*******************
            @GET
            fun paginate(@Url next_page_url:String):Call<EventListResponse>



}
