package com.clint.nasa.features.pictures

import com.clint.nasa.AndroidTest
import com.clint.nasa.core.functional.Either
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class PicturesViewModelTest : AndroidTest() {

    private lateinit var picturesViewModel: PicturesViewModel

    @MockK
    private lateinit var getPictures: GetPictures

    @Before
    fun setup() {
        picturesViewModel = PicturesViewModel(getPictures)
    }

    @Test
    fun `loading pictures should update livedata`() {
        val picturesList = listOf(
            Pictures(title = "Starbust Galaxy", url = "url_1"),
            Pictures(title = "Dumbell Nebula", url = "url_2")
        )
        coEvery { getPictures.run(any()) } returns Either.Right(picturesList)
        picturesViewModel.pictures.observeForever {
            it.apply {
                size shouldBeEqualTo 2
                it[0].title shouldBeEqualTo "Starbust Galaxy"
                it[0].url shouldBeEqualTo "url_1"
                it[1].title shouldBeEqualTo "Dumbell Nebula"
                it[1].url shouldBeEqualTo "url_2"
            }
        }
        runBlocking { picturesViewModel.loadPictures() }
    }
}