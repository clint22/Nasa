package com.clint.nasa.pictures

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.clint.nasa.R
import com.clint.nasa.core.Constants.SPLASH_SCREEN_DELAY
import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.extensions.failure
import com.clint.nasa.core.extensions.observe
import com.clint.nasa.databinding.ActivityMainBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var picturesAdapter: PicturesAdapter
    private var keepSplashOnScreen = true
    private val picturesViewModel: PicturesViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseView()
        Handler(Looper.getMainLooper()).postDelayed(
            { keepSplashOnScreen = false }, SPLASH_SCREEN_DELAY
        )
        loadPictures()
        with(picturesViewModel) {
            observe(pictures, ::renderPictures)
            failure(failure, ::renderFailure)
        }
    }

    private fun initialiseView() {
        binding.recyclerViewCars.run {
            setVeilLayout(R.layout.row_pictures)
            setAdapter(picturesAdapter)
            setLayoutManager(GridLayoutManager(this@MainActivity, 2))
            addVeiledItems(15)
        }
        /*binding.recyclerViewCars.setAdapter(picturesAdapter)
        binding.recyclerViewCars.setLayoutManager(GridLayoutManager(this, 2))
        binding.recyclerViewCars.addVeiledItems(15)
        binding.recyclerViewCars.veil()*/
    }

    private fun renderFailure(failure: Failure?) {
        Log.e("failure_occurred", "occurred")
    }

    private fun renderPictures(pictures: List<Pictures>?) {
        Log.e("nasa_pictures", Gson().toJson(pictures))
//        binding.progressPictures.visibility = View.GONE
//        binding.recyclerViewCars.visibility = View.VISIBLE
        picturesAdapter.picturesList = pictures.orEmpty()
        binding.recyclerViewCars.unVeil()
    }

    private fun loadPictures() {
        picturesViewModel.loadPictures()
    }
}