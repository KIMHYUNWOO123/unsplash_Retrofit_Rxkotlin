package com.example.rxkotlin_retrofit_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rxkotlin_retrofit_practice.Retrofit.RetrofitManager
import com.example.rxkotlin_retrofit_practice.utils.STATE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            RetrofitManager.instance.searhPhotos(searchTerm = input.text.toString(), completion = {
                state, body ->
                when(state){
                    STATE.OKAY ->{
                        Glide.with(this)
                            .load(body)
                            .into(imageView)
                    }
                    STATE.FAIL ->{
                        Toast.makeText(this, "에러입니다",Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }
}