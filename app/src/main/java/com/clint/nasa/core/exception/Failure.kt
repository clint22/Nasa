package com.clint.nasa.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
}