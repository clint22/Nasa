package com.clint.nasa.features.pictures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clint.nasa.R
import com.clint.nasa.core.Constants.INTENT_KEY_DESCRIPTION_DETAIL_EXTRA_NAME
import com.clint.nasa.databinding.FragmentDescriptionDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PicturesDescriptionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDescriptionDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionDialogBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            arguments?.getString(INTENT_KEY_DESCRIPTION_DETAIL_EXTRA_NAME)?.let {
                binding.textViewDetailedDescription.text = it
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SheetDialog)
    }
}