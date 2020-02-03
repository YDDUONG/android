package com.example.gallery

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

//辅助类，单例模式，网络请求只有一个队列，（只允许有一个对象）
class VolleySingleton private constructor(context : Context){
    companion object { //静态方法，类名直接调用
        private var INSTANCE : VolleySingleton?= null
        fun getInstance(context: Context) =
            INSTANCE?: synchronized(this) { //防止多线程
                VolleySingleton(context).also { INSTANCE = it }
        }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
        //可以在外面通过...
    }
}