package com.godrobles.groovy.playlist

import com.godrobles.groovy.utils.BaseUnitTest
import com.godrobles.groovy.utils.captureValues
import com.godrobles.groovy.utils.getValueForTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.RuntimeException

class PlaylistViewModelShould : BaseUnitTest() {

    private val repository: PlaylistRepository = mock()
    private val playlist = mock<List<Playlist>>()
    private val expected = Result.success(playlist)
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getPlaylistFromRepository() = runTest {
        val viewModel = mockSuccesfullCase()

        viewModel.playlist.getValueForTest()

        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun emitsPlaylistFromRepository() = runTest {
        val viewModel = mockSuccesfullCase()

        assertEquals(expected, viewModel.playlist.getValueForTest())
    }

    @Test
    fun emitErrorWhenReceiveError() {
        val viewModel = mockErrorCase()

        assertEquals(exception, viewModel.playlist.getValueForTest()!!.exceptionOrNull())
    }

    @Test
    fun showLoaderWhileLoading() = runTest {
        val viewModel = mockSuccesfullCase()

        viewModel.loader.captureValues {
            viewModel.playlist.getValueForTest()

            assertEquals(true, values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistLoad() = runTest {
        val viewModel = mockSuccesfullCase()

        viewModel.loader.captureValues {
            viewModel.playlist.getValueForTest()

            assertEquals(false, values.last())
        }
    }

    @Test
    fun closeLoaderAfterError() = runTest {
        val viewModel = mockErrorCase()

        viewModel.loader.captureValues {
            viewModel.playlist.getValueForTest()

            assertEquals(false, values.last())
        }
    }

    private fun mockSuccesfullCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }

        return PlaylistViewModel(repository)
    }

    private fun mockErrorCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(Result.failure<List<Playlist>>(exception))
                }
            )
        }

        return PlaylistViewModel(repository)
    }
}