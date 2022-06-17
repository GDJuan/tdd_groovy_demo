package com.godrobles.groovy.playlist

import com.godrobles.groovy.utils.BaseUnitTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PlaylistRepositoryShould : BaseUnitTest() {

    private val service: PlaylistService = mock()
    private val playlist = mock<List<Playlist>>()
    private val mapper: PlaylistMapper = mock()
    private val playlistRaw = mock<List<PlaylistRaw>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getPlaylistFromService() = runTest {

        val repository = mockSuccessfulCase()
        repository.getPlaylists()

        verify(service, times(1)).fetchPlaylist()
    }

    @Test
    fun emitMappedPlaylistsFromService() = runTest {
        val repository = mockSuccessfulCase()

        assertEquals(playlist, repository.getPlaylists().first().getOrNull())
    }

    @Test
    fun propagateErrors() = runTest {
        val repository = mockFailureCase()

        assertEquals(exception, repository.getPlaylists().first().exceptionOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper() = runTest {
        val repository = mockSuccessfulCase()

        repository.getPlaylists().first()

        verify(mapper, times(1)).invoke(playlistRaw)
    }

    private suspend fun mockFailureCase(): PlaylistRepository {
        whenever(service.fetchPlaylist()).thenReturn(
            flow {
                emit(Result.failure<List<PlaylistRaw>>(exception))
            }
        )

        return PlaylistRepository(service, mapper)
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylist()).thenReturn(
            flow {
                emit(Result.success(playlistRaw))
            }
        )

        whenever(mapper.invoke(playlistRaw)).thenReturn(playlist)

        return PlaylistRepository(service, mapper)
    }
}