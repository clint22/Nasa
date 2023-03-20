package com.clint.nasa.features.pictures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentTransaction
import com.clint.nasa.R
import com.clint.nasa.core.Constants.INTENT_KEY_DESCRIPTION_DETAIL_EXTRA_NAME
import com.clint.nasa.core.Constants.INTENT_KEY_PICTURE_EXTRA_NAME
import com.clint.nasa.core.Constants.INTENT_KEY_PICTURE_EXTRA_POSITION_NAME
import com.clint.nasa.core.Constants.ITEM_TRANSITION_MIN_SCALE
import com.clint.nasa.core.Constants.ITEM_TRANSITION_TIME_IN_MILLIS
import com.clint.nasa.core.discriteScrollView.DSVOrientation
import com.clint.nasa.core.discriteScrollView.DiscreteScrollView
import com.clint.nasa.core.discriteScrollView.InfiniteScrollAdapter
import com.clint.nasa.core.discriteScrollView.transform.ScaleTransformer
import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.extensions.*
import com.clint.nasa.databinding.ActivityPicturesDetailBinding
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PicturesDetailActivity : AppCompatActivity(),
    DiscreteScrollView.OnItemChangedListener<PicturesAdapterScroll.ViewHolder> {
    @Inject
    lateinit var picturesAdapter: PicturesAdapterScroll
    private lateinit var binding: ActivityPicturesDetailBinding
    private lateinit var infiniteAdapter: InfiniteScrollAdapter<*>
    private val picturesViewModel: PicturesViewModel by viewModels()
    private var pictures: List<Pictures>? = null
    private var picture: Pictures? = null
    private var loadedFirstTime = true
    private var picturePosition: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            this.setTheme(R.style.Theme_Nasa)
        }
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            installSplashScreen()
        }
        binding = ActivityPicturesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseView()
        loadPictures()
        setClickListeners()
        with(picturesViewModel) {
            observe(pictures, ::renderPictures)
            failure(failure, ::renderFailure)
        }
    }

    private fun setClickListeners() {
        binding.textViewReadMore.setOnClickListener {
            val picturesDescriptionFragment = PicturesDescriptionFragment()
            val bundle = Bundle()
            bundle.putString(INTENT_KEY_DESCRIPTION_DETAIL_EXTRA_NAME, picture?.explanation)
            picturesDescriptionFragment.arguments = bundle
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            picturesDescriptionFragment.show(ft, "dialog")
        }
        binding.imageViewBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun renderFailure(failure: Failure?) {
        binding.apply {
            progressDiscreetScroll.visibility = View.GONE
            textViewRelatedError.visibility = View.VISIBLE
        }
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
    }

    private fun renderPictures(pictures: List<Pictures>?) {
        this.pictures = pictures
        picturesAdapter.picturesList = pictures.orEmpty()
        binding.run {
            picturesScrollView.setOrientation(DSVOrientation.HORIZONTAL)
            picturesScrollView.addOnItemChangedListener(this@PicturesDetailActivity)
            infiniteAdapter = InfiniteScrollAdapter.wrap(picturesAdapter)
            picturesScrollView.adapter = infiniteAdapter
            picturesScrollView.setItemTransitionTimeMillis(ITEM_TRANSITION_TIME_IN_MILLIS)
            picturesScrollView.setItemTransformer(
                ScaleTransformer.Builder().setMinScale(ITEM_TRANSITION_MIN_SCALE).build()
            )
            picturesScrollView.scrollToPosition(picturePosition)
            progressDiscreetScroll.visibility = View.GONE
            picturesScrollView.visibility = View.VISIBLE
        }
    }

    private fun loadPictures() {
        picturesViewModel.loadPictures()
    }

    private fun initialiseView() {
        intent.getIntExtra(pictureExtraPositionName, 0).let {
            picturePosition = it
        }
        intent.parcelable<Pictures>(pictureExtraName)?.let {
            picture = it
            picture?.apply {
                setupDetailView(url, title, explanation)
            }
        }
    }

    private fun setupDetailView(url: String?, title: String?, explanation: String?) {
        binding.run {
            detailImageView.loadFromUrl(url)
            detailTitleTextView.text = title
            detailedDescriptionTextView.text = explanation
        }
    }

    companion object {
        private const val pictureExtraName = INTENT_KEY_PICTURE_EXTRA_NAME
        private const val pictureExtraPositionName = INTENT_KEY_PICTURE_EXTRA_POSITION_NAME
        fun startActivity(
            context: Context,
            transformationLayout: TransformationLayout,
            pictures: Pictures,
            position: Int
        ) {
            val intent = Intent(context, PicturesDetailActivity::class.java)
            intent.putExtra(pictureExtraName, pictures)
            intent.putExtra(pictureExtraPositionName, position)
            TransformationCompat.startActivity(transformationLayout, intent)

        }
    }

    override fun onCurrentItemChanged(
        viewHolder: PicturesAdapterScroll.ViewHolder?, adapterPosition: Int
    ) {
        if (!loadedFirstTime) {
            val positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition)
            picture = pictures?.get(positionInDataSet)
            picture?.apply {
                setupDetailView(
                    url = url, title = title, explanation = explanation
                )
            }
        }
        loadedFirstTime = false
    }
}