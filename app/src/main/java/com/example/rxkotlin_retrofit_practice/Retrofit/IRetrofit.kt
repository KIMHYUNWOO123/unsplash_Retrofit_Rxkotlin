package com.example.rxkotlin_retrofit_practice.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IRetrofit {
//    @Headers("client_id=PO5UUsj6llBSVjg-Ea-wJDRQHUQmusLNG1f0qF2IzfA")
    @GET("/search/photos")
    fun searchImagePhotos(
        @Query("query") searchTerm : String,
        @Query("client_id") key: String
    ) : Call<Result>



}