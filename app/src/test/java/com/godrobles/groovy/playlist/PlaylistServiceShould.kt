package com.godrobles.groovy.playlist

import com.godrobles.groovy.utils.BaseUnitTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PlaylistServiceShould : BaseUnitTest() {

    private lateinit var service: PlaylistService
    private val playlistAPI: PlaylistAPI = mock()
    private val allPlaylist = mock<List<PlaylistRaw>>()

    @Test
    fun fetchPlaylistFromAPI() = runTest {

        service = PlaylistService(playlistAPI)
        service.fetchPlaylist().first()

        verify(playlistAPI, times(1)).fetchAllPlaylists()
    }

    @Test
    fun convertValuesToFlowResultAndEmitsThem() = runTest {
        mockSuccesfulCase()

        assertEquals(Result.success(allPlaylist), service.fetchPlaylist().first())
    }

    @Test
    fun emitsErrorResultWhenNetworkFails() = runTest {
        mockFailureCase()

        assertEquals("Something went wrong", service.fetchPlaylist().first().exceptionOrNull()?.message)
    }

    private suspend fun mockSuccesfulCase() {
        whenever(playlistAPI.fetchAllPlaylists()).thenReturn(
            allPlaylist
        )

        service = PlaylistService(playlistAPI)
    }

    private suspend fun mockFailureCase() {
        whenever(playlistAPI.fetchAllPlaylists()).thenThrow(RuntimeException("Network connection error"))

        service = PlaylistService(playlistAPI)
    }

}