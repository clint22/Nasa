package com.clint.nasa.features.pictures

import com.clint.nasa.UnitTest
import com.clint.nasa.core.functional.Either
import com.clint.nasa.core.interactor.UseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPicturesTest : UnitTest() {

    private lateinit var getPictures: GetPictures

    @MockK
    private lateinit var picturesRepository: PicturesRepository

    @Before
    fun setup() {
        getPictures = GetPictures(picturesRepository)
        every { picturesRepository.pictures() } returns Either.Right(listOf(Pictures.empty))
    }

    @Test
    fun `should get data from the repository`() {
        runBlocking { getPictures.run(UseCase.None()) }
        verify(exactly = 1) { picturesRepository.pictures() }
    }
}