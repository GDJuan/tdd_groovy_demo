package com.godrobles.groovy.playlist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.godrobles.groovy.R
import com.godrobles.groovy.databinding.FragmentPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private val viewModel by viewModels<PlaylistViewModel>()
    private var _binding: FragmentPlaylistBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPlaylistBinding.bind(view)

        runObservers()
    }

    private fun runObservers() {

        viewModel.loader.observe(this as LifecycleOwner) { loading ->
            when(loading) {
                true -> _binding?.loader?.visibility = View.VISIBLE
                else -> _binding?.loader?.visibility = View.GONE
            }
        }

        viewModel.playlist.observe(this as LifecycleOwner) { playlist ->
            if (playlist.getOrNull() != null)
                setupList(_binding?.playlistList, playlist.getOrNull()!!)
            else {
                // TODO
            }
        }
    }

    private fun setupList(view: View?, playlist: List<Playlist>) {
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyPlaylistRecyclerViewAdapter(playlist)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PlaylistFragment().apply {}
    }
}