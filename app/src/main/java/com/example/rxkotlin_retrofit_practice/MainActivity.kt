package com.example.rxkotlin_retrofit_practice

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rxkotlin_retrofit_practice.Retrofit.RetrofitManager
import com.example.rxkotlin_retrofit_practice.utils.STATE
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var myThread : MyThread? = null
    lateinit var myHandler : MyHandler
    private var myCompositeDisposable : CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myHandler = MyHandler()
        myThread = MyThread()
        myThread!!.start()
    }
    inner class MyThread : Thread(){
        override fun run() {
            super.run()
            Log.d("Thread", "run: Clear")
            textChange()
        }
    }
    inner class MyHandler : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                0 -> {
                    Toast.makeText(this@MainActivity ,"에러입니다",Toast.LENGTH_LONG).show()
                }
                1 -> {
                    Glide.with(this@MainActivity)
                        .load(msg.obj)
                        .into(imageView)
                    Log.d("handler", "handleMessage: ")
                }
            }
        }
    }
    fun textChange() {
        var myObservable = input.textChanges()
        var myObserve : Disposable =
            myObservable
                .debounce(1500,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onNext = {
                        Log.d("ob", "textChange: $it")
                        var message : Message = Message.obtain()
                        if(it.isNotEmpty()) {
                            RetrofitManager.instance.searhPhotos(
                                searchTerm = input.text.toString(),
                                completion = { state, body ->
                                    when (state) {
                                        STATE.OKAY -> {
                                            message.what = 1
                                            message.obj = body
                                            myHandler.sendMessage(message)
                                        }
                                        STATE.FAIL -> {
                                            myHandler.sendEmptyMessage(0)
                                        }
                                    }
                                })
                        }
                    },
                    onComplete = {
                        Log.d("ob", "textChange: Complete")
                    },
                    onError = {
                        Log.d("ob", "textChange: $it ")
                    }
                )
        myCompositeDisposable?.add(myObserve)
    }

    override fun onDestroy() {
        myCompositeDisposable?.clear()
        super.onDestroy()
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}