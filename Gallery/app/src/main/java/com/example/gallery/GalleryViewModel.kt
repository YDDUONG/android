package com.example.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<Photo>>() //LiveData数据类型
    val photoListLive : LiveData<List<Photo>>
    get() = _photoListLive //数据封装

    // 图片关键词
    private val keyWords = arrayOf("girl", "dog", "cat", "phone")

    //获取网络地址
    private fun getUrl(): String {
        //https://pixabay.com/api/?key=15115748-c12fc92e4837ad74798d81bb8&q=yellow+flowers&image_type=photo&pretty=true
        return "https://pixabay.com/api/?key=15115748-c12fc92e4837ad74798d81bb8&q=${keyWords.random()}&per_page=30"
    }

    //获取数据
    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(), //地址
            Response.Listener { //返回成功
                _photoListLive.value = Gson().fromJson(it, Pixabay::class.java).hits.toList()
            },
            Response.ErrorListener { //返回失败
                Log.d("hello", it.toString())
            }
        )
        //添加到网络请求队列
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }
}