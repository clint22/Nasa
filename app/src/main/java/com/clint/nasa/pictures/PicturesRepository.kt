package com.clint.nasa.pictures

import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.functional.Either
import com.clint.nasa.core.platform.NetworkHandler
import retrofit2.Call
import javax.inject.Inject

interface PicturesRepository {

    fun pictures(): Either<Failure, List<Pictures>>
}

class NetWork
@Inject constructor(
    private val networkHandler: NetworkHandler,
    private val pictureService: PictureService
) : PicturesRepository {

    override fun pictures(): Either<Failure, List<Pictures>> {
        return when (networkHandler.isNetworkAvailable()) {
            true -> {
                request(
                    pictureService.getPictures(), {
                        it
                    },
                    mutableListOf()
                )
            }
            false -> {
                Either.Left(Failure.NetworkConnection)
            }
        }
    }

}

private fun <T, R> request(
    call: Call<T>,
    transform: (T) -> R,
    default: T
): Either<Failure, R> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> {
                Either.Right(transform((response.body() ?: default)))
            }
            false -> Either.Left(Failure.ServerError)
        }
    } catch (exception: Throwable) {
        Either.Left(Failure.ServerError)
    }
}