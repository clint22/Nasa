package com.clint.nasa.pictures

import retrofit2.Call
import retrofit2.http.GET

internal interface PicturesApi {
    @GET("6efb3da58234c1f63915")
    fun getPictures(): Call<List<Pictures>>
}