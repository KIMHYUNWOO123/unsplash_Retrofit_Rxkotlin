package com.example.rxkotlin_retrofit_practice.Retrofit

import android.util.Log
import com.bumptech.glide.Glide
import com.example.rxkotlin_retrofit_practice.utils.API
import com.example.rxkotlin_retrofit_practice.utils.Constants.TAG
import com.example.rxkotlin_retrofit_practice.utils.STATE
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitManager {
    companion object {
        var instance = RetrofitManager()
    }

    private val iRetrofit: IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun searhPhotos(searchTerm : String, completion : (STATE, String) -> Unit){
        var Term = searchTerm ?: ""

        val call = iRetrofit?.searchImagePhotos(Term,API.KEY) ?: return

        call.enqueue(object: Callback<Result>{
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                var url = response.body()?.results

                Log.d(TAG, "onResponse: 응답성공 ${url?.get(0)?.urls?.raw}")
                completion(STATE.OKAY,url?.get(0)?.urls?.raw.toString())


            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d(TAG, "onFailure: 응답 실패 $t" )
                completion(STATE.FAIL, t.toString())
            }

        })

    }

}