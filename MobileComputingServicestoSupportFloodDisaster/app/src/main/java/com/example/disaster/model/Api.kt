package com.example.disaster.model


import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {


    @FormUrlEncoded
    @POST("floodRequest.php")
    fun sendRequest(
        @Field("user_id") user_id: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("location") location: String,
        @Field("status") status: String,
        @Field("created_at") created_at: String,
        @Field("isVerified") isVerified: String,
        @Field("image") image: String,
    ): Call<LoginResponse>


    @FormUrlEncoded
    @POST("updateRequest.php")
    fun updateRequest(
        @Field("status") status: String,
        @Field("id") id: Int,
    ): Call<LoginResponse>

    @GET("get_requests.php")
    fun getRequests(): Call<LoginResponse>


    @GET("getDonationRequest.php")
    fun getDonationRequests(): Call<Userresponse>


    @FormUrlEncoded
    @POST("users.php")
    fun register(
        @Field("name") name: String,
        @Field("num") num: String,
        @Field("email") email: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("pass") pass: String,
        @Field("type") type: String,
        @Field("status") status: String,
        @Field("astatus") astatus: String,
        @Field("condition") condition: String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("activities.php")
    fun uploadingactivity(
        @Field("donorId") id: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("description") description: String,
        @Field("type") type: String,
        @Field("photo") photo: String,
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun login(
        @Field("email") email: String,
        @Field("pass") pass: String,
        @Field("condition") condition: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun updateprofile(
        @Field("name") name: String,
        @Field("num") num: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("pass") pass: String,
        @Field("type") type: String,
        @Field("status") status: String,
        @Field("id") id: Int, @Field("condition") condition: String,
    ): Call<DefaultResponse>


    @GET("getuser.php")
    fun adminuser(
        @Query("type") type: String,
    ): Call<Userresponse>


    @GET("getuser.php")
    fun admindonors(
        @Query("type") condition: String,
    ): Call<Userresponse>

    @GET("getsupport.php")
    fun adminsupporters(): Call<Userresponse>

    @FormUrlEncoded
    @POST("users.php")
    fun Deleteperson(
        @Field("id") id: Int, @Field("condition") condition: String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("update.php")
    fun ngoadedd(
        @Field("condition") condition: String,
        @Field("id") id: Int,
        @Field("details") details: String,
    ): Call<CommonResponse>


    @GET("getDonoRequest.php")
    fun gettingdetails(): Call<DoneeStatus>


    @FormUrlEncoded
    @POST("updatedonation.php")
    fun updateDonationDetails(
        @Field("id") id: Int,
        @Field("someField") status: String,
        @Field("receiverId") receiverId: String,
        @Field("receiverName") receiverName: String,
    ): Call<DefaultResponse>


}