package com.clint.nasa.pictures

import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.functional.Either
import com.clint.nasa.core.interactor.UseCase
import javax.inject.Inject

class GetPictures
@Inject constructor(private val picturesRepository: PicturesRepository) :
    UseCase<List<Pictures>, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, List<Pictures>> {
        return picturesRepository.pictures()
    }
}