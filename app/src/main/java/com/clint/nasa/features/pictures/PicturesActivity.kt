package com.clint.nasa.features.pictures

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.clint.nasa.R
import com.clint.nasa.core.Constants.GRID_SPAN_COUNT
import com.clint.nasa.core.Constants.SPLASH_SCREEN_DELAY
import com.clint.nasa.core.Constants.VEILED_ITEMS
import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.extensions.failure
import com.clint.nasa.core.extensions.observe
import com.clint.nasa.core.extensions.showToast
import com.clint.nasa.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PicturesActivity : AppCompatActivity() {
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
        binding.recyclerViewPictures.run {
            setVeilLayout(R.layout.row_pictures)
            setAdapter(picturesAdapter)
            setLayoutManager(GridLayoutManager(this@PicturesActivity, GRID_SPAN_COUNT))
            addVeiledItems(VEILED_ITEMS)
        }
    }

    private fun renderFailure(failure: Failure?) {
        when (failure) {
            Failure.NetworkConnection -> {
                showToast(getString(R.string.network_error))
            }
            Failure.ServerError -> {
                showToast(getString(R.string.server_error))
            }
            else -> {
                showToast(getString(R.string.some_error))
            }
        }
        binding.apply {
            lottieError.visibility = View.VISIBLE
            recyclerViewPictures.visibility = View.GONE
        }
    }

    private fun renderPictures(pictures: List<Pictures>?) {
        picturesAdapter.picturesList = pictures.orEmpty()
        binding.apply {
            recyclerViewPictures.visibility = View.VISIBLE
            lottieError.visibility = View.GONE
            recyclerViewPictures.unVeil()
        }
    }

    private fun loadPictures() {
        picturesViewModel.loadPictures()
    }
}