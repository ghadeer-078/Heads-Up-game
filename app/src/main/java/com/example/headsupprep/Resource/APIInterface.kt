package com.example.headsupprep.Resource

import com.example.headsupprep.Model.AddCelebrity
import com.example.headsupprep.Model.CelebrityGame
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @GET("/celebrities/")
    fun showInfo(): Call <List<CelebrityGame?>>

    @POST("/celebrities/")
    fun addInfo(@Body newUbring: AddCelebrity): Call<AddCelebrity>

    @PUT("/celebrities/{pk}")
    fun updateInfo(@Path("pk") pk: Int, @Body updateUbring: CelebrityGame): Call<CelebrityGame>

    @DELETE("/celebrities/{pk}")
    fun deleteInfo(@Path("pk") pk: Int): Call<Void>

}