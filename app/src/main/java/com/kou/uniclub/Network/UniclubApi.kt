package com.kou.uniclub.Network

import com.kou.uniclub.Model.Auth.LoginResponse
import com.kou.uniclub.Model.Auth.SignUpResponse
import com.kou.uniclub.Model.Club.NoPagination.ClubDetailsResponse
import com.kou.uniclub.Model.Club.NoPagination.ClubsByUnivResponse
import com.kou.uniclub.Model.Club.Pagination.ClubsResponse
import com.kou.uniclub.Model.Event.NoPagination.EventDetailsResponse
import com.kou.uniclub.Model.Event.NoPagination.EventsResponse
import com.kou.uniclub.Model.Event.Pagination.EventListResponse
import com.kou.uniclub.Model.Notification.NotificationResponse
import com.kou.uniclub.Model.Notification.NotifsActionsResponse
import com.kou.uniclub.Model.University.NoPagination.UniversitiesResponse
import com.kou.uniclub.Model.University.Pagination.UniversityResponse
import com.kou.uniclub.Model.User.Behaviour.FavoriteResponse
import com.kou.uniclub.Model.User.Behaviour.FollowResponse
import com.kou.uniclub.Model.User.Behaviour.MyfavoritesResponse
import com.kou.uniclub.Model.User.Behaviour.ParticipateResponse
import com.kou.uniclub.Model.User.UserX
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UniclubApi {

    companion object Factory {
        var imageURL = "http://192.168.1.4:8000"
        fun create(): UniclubApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.1.4:8000/api/")//10.0.2.2:8000 emulator //put ipv4 adress//me192.168.1.4//orange 10.54.234.189
                .build()
            return retrofit.create(UniclubApi::class.java)
        }
    }

    /************************* User ********************/
    @Multipart
    @POST("auth/signup")
    fun signUP(
        @Query("First_Name") fname: String,
        @Query("Last_Name") lname: String,
        @Query("Birth_Date") birth: String?,
        @Query("Email") email: String,
        @Query("password") pass: String,
        @Query("password_confirmation") passC: String,
        @Query("Adress") address: String,
        @Part file: MultipartBody.Part?
    ): Call<SignUpResponse>


    @FormUrlEncoded
    @POST("auth/login")
    fun signIN(
        @Field("Email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("auth/user")
    fun getUser(@Header("Authorization") authToken: String): Call<UserX>

    /************************* User favorites ********************/

    @POST("auth/favourite/{id}")
    fun favorite(@Header("Authorization") authToken: String, @Path("id") eventID: Int): Call<FavoriteResponse>

    @DELETE("auth/unfavourite/{id}")
    fun unfavorite(@Header("Authorization") authToken: String, @Path("id") eventID: Int): Call<FavoriteResponse>

    @GET("auth/getUserFavouriteEvents")
    fun getFavorites(@Header("Authorization") authToken: String): Call<MyfavoritesResponse>

    /************************* User Participations ********************/

    @POST("auth/participate/{id}")
    fun participate(@Header("Authorization") authToken: String, @Path("id") eventID: Int): Call<ParticipateResponse>

    @DELETE("auth/leave/{id}")
    fun leave(@Header("Authorization") authToken: String, @Path("id") eventID: Int): Call<ParticipateResponse>

    @GET("auth/getUserParticipatedEvents")
    fun getParticipations(@Header("Authorization") authToken: String): Call<EventListResponse>

    /************************* User Follows ********************/
    @POST("auth/follow/{id}")
    fun follow(@Header("Authorization") authToken: String, @Path("id") clubID: Int): Call<FollowResponse>

    @DELETE("auth/unfollow/{id}")
    fun unfollow(@Header("Authorization") authToken: String, @Path("id") clubID: Int): Call<FollowResponse>

    @GET("auth/getUserFollowedClubs")
    fun getFollows(@Header("Authorization") authToken: String): Call<ClubsResponse>

    /************************* Notifications ********************/
    @GET("auth/getNotif")
    fun getNotifs(@Header("Authorization") authToken: String): Call<NotificationResponse>

    @GET("auth/markRead/{id}")
    fun markAsRead(@Header("Authorization") authToken: String, @Path("id") notifId: String): Call<NotifsActionsResponse>
    @DELETE("auth/deleteNotif/{id}")
    fun deleteNotif(@Header("Authorization") authToken: String, @Path("id") notifId: String): Call<NotifsActionsResponse>

    @GET("auth/markAllRead")
    fun markAllRead(@Header("Authorization") authToken: String): Call<NotifsActionsResponse>


    /************************* Events ********************/

    @GET("Event/List")
    fun getEvents(): Call<EventsResponse>

    @GET("Event/List/Paginated")
    fun getEventsP(): Call<EventListResponse>

    @GET
    fun paginateEvents(@Url next_page_url: String): Call<EventListResponse>

    @GET("EventDetails/{id}")
    fun getEvent(@Path("id") eventID: Int): Call<EventDetailsResponse>

    @GET("Event/Upcoming")
    fun getUpcomingEvents(): Call<EventListResponse>

    @GET("Event/today")
    fun getTodayEvents(): Call<EventListResponse>

    @GET("Event/Passed")
    fun getPassedEvents(): Call<EventListResponse>

    @GET("Event/showByRegion/{region}")
    fun showByRegion(@Path("region") re: String): Call<EventListResponse>


    /************************* University ********************/

    @GET("University/List ")
    fun getUniversities(): retrofit2.Call<UniversitiesResponse>

    @GET
    fun paginateUnivs(@Url next_page_url: String): Call<UniversityResponse>

    /************************* Clubs ********************/
    @GET
    fun paginateClubs(@Url next_page_url: String): Call<ClubsResponse>

    @GET("Club/List/Paginated")
    fun getClubs(): Call<ClubsResponse>

    @GET("Club/showByUniversity/{univ_id}")
    fun getClubsByUniv(@Path("univ_id") id: Int): Call<ClubsByUnivResponse>


    @GET("Club/{id}/UpcomingEvents")
    fun getClubUpcomingE(@Path("id") id: Int): Call<EventListResponse>

    @GET("ClubDetails/{id}")
    fun getClub(@Path("id") id: Int): Call<ClubDetailsResponse>


}
