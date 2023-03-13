package com.clint.nasa.pictures

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.clint.nasa.R
import com.clint.nasa.core.Constants.SPLASH_SCREEN_DELAY
import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.extensions.failure
import com.clint.nasa.core.extensions.observe
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var keepSplashOnScreen = true
    private val picturesViewModel: PicturesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler(Looper.getMainLooper()).postDelayed(
            { keepSplashOnScreen = false }, SPLASH_SCREEN_DELAY
        )
        loadPictures()
        with(picturesViewModel) {
            observe(pictures, ::renderPictures)
            failure(failure, ::renderFailure)
        }
    }

    private fun renderFailure(failure: Failure?) {
        Log.e("failure_occurred", "occurred")
    }

    private fun renderPictures(pictures: List<Pictures>?) {
        Log.e("nasa_pictures", Gson().toJson(pictures))
    }

    private fun loadPictures() {
        picturesViewModel.loadPictures()
    }
}