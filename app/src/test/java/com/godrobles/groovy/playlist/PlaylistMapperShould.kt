package com.godrobles.groovy.playlist

import com.godrobles.groovy.R
import com.godrobles.groovy.utils.BaseUnitTest
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PlaylistMapperShould : BaseUnitTest() {

    private val playlistRaw = PlaylistRaw("1", "Say may name", "cat")
    private val playlistRawRock = PlaylistRaw("1", "Say may name", "rock")
    private val mapper = PlaylistMapper()
    private val playlists = mapper(listOf(playlistRaw))
    private val playlist = playlists[0]
    private val playlistRock = mapper(listOf(playlistRawRock))[0]

    @Test
    fun keepSameId() {
        assertEquals(playlistRaw.id, playlist.id)
    }

    @Test
    fun keepSameName() {
        assertEquals(playlistRaw.name, playlist.name)
    }

    @Test
    fun keepSameCategory() {
        assertEquals(playlistRaw.category, playlist.category)
    }

    @Test
    fun mapDefaultImageWhenNotRock() {
        assertEquals(R.mipmap.playlist, playlist.image)
    }

    @Test
    fun mapRockImageWhenRock() {
        assertEquals(R.mipmap.rock, playlistRock.image)
    }
}