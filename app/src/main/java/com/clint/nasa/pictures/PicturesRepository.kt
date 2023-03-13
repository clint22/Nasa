package com.clint.nasa.pictures

import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.functional.Either
import com.clint.nasa.core.functional.Request.request
import com.clint.nasa.core.platform.NetworkHandler
import javax.inject.Inject

interface PicturesRepository {

    fun pictures(): Either<Failure, List<Pictures>>
}

class NetWork
@Inject constructor(
    private val networkHandler: NetworkHandler, private val pictureService: PictureService
) : PicturesRepository {

    override fun pictures(): Either<Failure, List<Pictures>> {
        return when (networkHandler.isNetworkAvailable()) {
            true -> {
                request(
                    pictureService.getPictures(), {
                        it
                    }, mutableListOf()
                )
            }
            false -> {
                Either.Left(Failure.NetworkConnection)
            }
        }
    }

}

