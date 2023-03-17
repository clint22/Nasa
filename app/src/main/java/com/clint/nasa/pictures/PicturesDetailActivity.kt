package com.clint.nasa.pictures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.clint.nasa.R
import com.clint.nasa.core.extensions.loadFromUrl
import com.clint.nasa.core.extensions.parcelable
import com.clint.nasa.databinding.ActivityPicturesDetailBinding
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout

class PicturesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPicturesDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            this.setTheme(R.style.Theme_Nasa);
        }
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            installSplashScreen()
        }
        binding = ActivityPicturesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.parcelable<Pictures>(pictureExtraName)?.let {
            binding.detailImageView.loadFromUrl(it.url)
            binding.detailTitleTextView.text = it.title
            binding.detailedDescriptionTextView.text = it.explanation
        }
    }

    companion object {
        private const val pictureExtraName = "pictureExtraName"
        fun startActivity(
            context: Context,
            transformationLayout: TransformationLayout,
            pictures: Pictures
        ) {
            val intent = Intent(context, PicturesDetailActivity::class.java)
            intent.putExtra(pictureExtraName, pictures)
            TransformationCompat.startActivity(transformationLayout, intent)

        }
    }
}