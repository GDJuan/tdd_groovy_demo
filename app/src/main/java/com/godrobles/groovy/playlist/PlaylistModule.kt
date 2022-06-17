package com.godrobles.groovy.playlist

import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

val client = OkHttpClient()
val idlingResource = OkHttp3IdlingResource.create("Okhttp", client)

@Module
@InstallIn(SingletonComponent::class)
class PlaylistModule {

    @Singleton
    @Provides
    fun retrofit() = Retrofit.Builder()
        .baseUrl("http://192.168.100.60:3000/") // Always check that it matches the local ip
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun playlistAPI(retrofit: Retrofit): PlaylistAPI = retrofit.create(PlaylistAPI::class.java)
}