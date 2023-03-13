package com.clint.nasa.pictures

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.clint.nasa.core.interactor.UseCase
import com.clint.nasa.core.platform.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PicturesViewModel
@Inject constructor(private val getPictures: GetPictures) : BaseViewModel() {

    private val _pictures: MutableLiveData<List<Pictures>> = MutableLiveData()
    val pictures = _pictures

    fun loadPictures() = getPictures(UseCase.None(), viewModelScope) {
        it.fold(::handleFailure, ::handlePictures)
    }

    private fun handlePictures(pictures: List<Pictures>) {
        _pictures.value = pictures
    }

}
