package com.godrobles.groovy.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaylistService @Inject constructor(
    private val playlistAPI: PlaylistAPI
) {

    suspend fun fetchPlaylist() : Flow<Result<List<PlaylistRaw>>> {
        return flow {
            emit(Result.success(playlistAPI.fetchAllPlaylists()))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }
}
