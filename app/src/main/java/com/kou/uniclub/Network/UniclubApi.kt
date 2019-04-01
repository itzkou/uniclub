package com.kou.uniclub.Network

import com.kou.uniclub.Model.ClubResponse
import com.kou.uniclub.Model.FeedResponse
import com.kou.uniclub.Model.Token
import com.kou.uniclub.Model.UniversityResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UniclubApi{

        companion object Factory {    //retrofit instance  //trick hetheya Apiutils
            fun create():UniclubApi {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://10.0.2.2:8000/api/")
                    .build()
                return retrofit.create(UniclubApi::class.java)
            }
        }


            @Multipart
            @POST("register")
            fun signUP(@Query("name") name:String,
                       @Query("email")email:String,
                       @Query("password")password:String,
                       @Query("category_id")c_id:Int,
                       @Query("status")s_id:Int,
                       @Part file: MultipartBody.Part) :Call<Token>
            @FormUrlEncoded
            @POST("login")
            fun signIN( @Field("email")email:String,
                        @Field("password")password:String):Call<Token>




            @GET("universities")
            fun getUniversities():retrofit2.Call<UniversityResponse>

            @GET("clubs/university/{univ_id}")
            fun getClubsByUniv(@Path("univ_id") id:Int): Call<ClubResponse>

            @GET("events")
            fun getEventFeed():Call<FeedResponse>
/*
@GET("events/club/{club_id}")
fun getEventByclub(@Path("club_id")id:Int):Call<EventResponse>
    @GET("clubs")
fun getClubs(): Call<ClubsResponse>*/

}
