package com.kou.uniclub.Network

import com.kou.uniclub.Model.UniversityResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

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

/*@GET("clubs")
fun getClubs(): Call<ClubsResponse>*/

        @GET("universities")
        fun getUniversities():retrofit2.Call<UniversityResponse>

/*@GET("clubs/university/{univ_id}")
fun getClubsByUniv(@Path("univ_id") id:Int):Call<ClubsResponse>

@GET("events/club/{club_id}")
fun getEventByclub(@Path("club_id")id:Int):Call<EventResponse>*/

}
