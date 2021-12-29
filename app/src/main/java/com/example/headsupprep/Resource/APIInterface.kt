package com.example.headsupprep.Resource

import com.example.headsupprep.Model.AddCelebrity
import com.example.headsupprep.Model.Celeb
import com.example.headsupprep.Model.CelebItem
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @POST("/celebrities/")
    fun addCeleb(@Body data: AddCelebrity): Call<CelebItem?>?

    @GET("/celebrities/")
    fun getCelebs(): Call<Celeb?>?

    @PUT("/celebrities/{id}")
    fun updateCeleb(@Path("id") id: Int, @Body celebData: CelebItem): Call<CelebItem>

    @DELETE("/celebrities/{id}")
    fun deleteCeleb(@Path("id") id: Int): Call<Void>?
}