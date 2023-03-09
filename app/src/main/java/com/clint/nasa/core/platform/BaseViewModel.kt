package com.clint.nasa.core.platform

import androidx.lifecycle.ViewModel
import com.clint.nasa.core.exception.Failure
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableSharedFlow<Failure> = MutableSharedFlow()
    val failure: MutableSharedFlow<Failure> = _failure

    protected suspend fun handleFailure(failure: Failure) {
        _failure.emit(failure)
    }
}