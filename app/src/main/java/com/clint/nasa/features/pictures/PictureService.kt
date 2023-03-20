package com.clint.nasa.features.pictures

import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PictureService
@Inject constructor(private val retrofit: Retrofit) : PicturesApi {
    private val picturesApi by lazy { retrofit.create(PicturesApi::class.java) }

    override fun getPictures(): Call<List<Pictures>> {
        return picturesApi.getPictures()
    }
}