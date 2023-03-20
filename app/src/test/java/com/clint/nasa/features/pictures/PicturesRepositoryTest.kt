package com.clint.nasa.features.pictures

import com.clint.nasa.UnitTest
import com.clint.nasa.core.exception.Failure
import com.clint.nasa.core.functional.Either
import com.clint.nasa.core.platform.NetworkHandler
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class PicturesRepositoryTest : UnitTest() {

    private lateinit var networkRepository: NetWork

    @MockK
    private lateinit var networkHandler: NetworkHandler

    @MockK
    private lateinit var pictureService: PictureService

    @MockK
    private lateinit var pictureResponse: Response<List<Pictures>>

    @MockK
    private lateinit var picturesCall: Call<List<Pictures>>

    @Before
    fun setup() {
        networkRepository = NetWork(networkHandler, pictureService)
    }

    @Test
    fun `should return empty list by default`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { pictureResponse.body() } returns null
        every { pictureResponse.isSuccessful } returns true
        every { picturesCall.execute() } returns pictureResponse
        every { pictureService.getPictures() } returns picturesCall
        val pictures = networkRepository.pictures()
        pictures shouldBeEqualTo Either.Right(emptyList())
        verify(exactly = 1) { pictureService.getPictures() }
    }

    @Test
    fun `should get pictures list from service`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { pictureResponse.body() } returns listOf(
            Pictures(
                title = "Starbust Galaxy",
                url = "url_1"
            )
        )
        every { pictureResponse.isSuccessful } returns true
        every { picturesCall.execute() } returns pictureResponse
        every { pictureService.getPictures() } returns picturesCall
        val pictures = networkRepository.pictures()
        pictures shouldBeEqualTo Either.Right(
            listOf(
                Pictures(
                    title = "Starbust Galaxy",
                    url = "url_1"
                )
            )
        )
        verify(exactly = 1) { pictureService.getPictures() }
    }

    @Test
    fun `picture service should return network failure when no connection`() {
        every { networkHandler.isNetworkAvailable() } returns false
        val pictures = networkRepository.pictures()
        pictures shouldBeInstanceOf Either::class.java
        pictures.isLeft shouldBeEqualTo true
        pictures.fold(
            { failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java },
            {})
        verify { pictureService wasNot called }
    }

    @Test
    fun `picture service should return server error when response is not successful`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { pictureResponse.isSuccessful } returns false
        every { picturesCall.execute() } returns pictureResponse
        every { pictureService.getPictures() } returns picturesCall
        val pictures = networkRepository.pictures()
        pictures shouldBeInstanceOf Either::class.java
        pictures.isLeft shouldBeEqualTo true
        pictures.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    @Test
    fun `picture request should capture exceptions`() {
        every { networkHandler.isNetworkAvailable() } returns true
        every { picturesCall.execute() } returns pictureResponse
        every { pictureService.getPictures() } returns picturesCall
        val pictures = networkRepository.pictures()
        pictures shouldBeInstanceOf Either::class.java
        pictures.isLeft shouldBeEqualTo true
        pictures.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

}