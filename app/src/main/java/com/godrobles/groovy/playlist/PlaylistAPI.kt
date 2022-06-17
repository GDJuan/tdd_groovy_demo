package com.godrobles.groovy.playlist

import retrofit2.http.GET

interface PlaylistAPI {

    @GET("playlist")
    suspend fun fetchAllPlaylists(): List<PlaylistRaw>
}
