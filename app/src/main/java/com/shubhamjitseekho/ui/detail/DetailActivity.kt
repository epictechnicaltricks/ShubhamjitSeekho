package com.shubhamjitseekho.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.shubhamjitseekho.R
import com.shubhamjitseekho.data.local.AppDatabase
import com.shubhamjitseekho.data.remote.RetrofitClient
import com.shubhamjitseekho.data.repository.AnimeRepositoryImpl
import com.shubhamjitseekho.databinding.ActivityDetailBinding
import com.shubhamjitseekho.domain.model.AnimeDetail
import com.shubhamjitseekho.ui.common.UiState
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var exoPlayer: ExoPlayer? = null
    private var isExpanded = false

    companion object {
        const val EXTRA_ANIME_ID = "extra_anime_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animeId = intent.getIntExtra(EXTRA_ANIME_ID, -1)
        if (animeId == -1) {
            finish()
            return
        }

        setupViewModel()
        setupClickListeners()
        observeAnimeDetail()
        viewModel.loadAnimeDetail(animeId)
    }

    private fun setupViewModel() {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = AnimeRepositoryImpl(
            api = RetrofitClient.api,
            dao = database.animeDao()
        )

        viewModel = DetailViewModel(repository)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tvSynopsis.setOnClickListener {
            toggleSynopsis()
        }

        binding.btnReadMore.setOnClickListener {
            toggleSynopsis()
        }
    }

    private fun observeAnimeDetail() {
        lifecycleScope.launch {
            viewModel.animeDetailState.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        hideLoading()
                    }

                    is UiState.Loading -> {
                        showLoading()
                    }

                    is UiState.Success -> {
                        hideLoading()
                        displayAnimeDetail(state.data)
                    }

                    is UiState.Error -> {
                        hideLoading()
                        showError(state.message)
                    }

                    is UiState.Empty -> {
                        hideLoading()
                        showError("Anime not found")
                    }
                }
            }
        }
    }

    private fun displayAnimeDetail(anime: AnimeDetail) {
        binding.apply {
            tvAnimeTitle.text = anime.title
            tvAnimeScore.text = anime.formattedScore
            tvAnimeEpisodes.text = anime.formattedEpisodes
            tvAnimeStatus.text = anime.status
            tvGenres.text = anime.genresText
            tvSynopsis.text = anime.synopsis

            Glide.with(this@DetailActivity)
                .load(anime.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_play)
                .error(R.drawable.ic_play)
                .into(ivAnimeHero)

            if (anime.hasTrailer) {
                setupVideoPlayer(anime.trailerUrl!!)
                playerView.visibility = View.VISIBLE
                ivAnimeHero.visibility = View.GONE
            } else {
                playerView.visibility = View.GONE
                ivAnimeHero.visibility = View.VISIBLE
            }

            contentGroup.visibility = View.VISIBLE
        }
    }

    private fun setupVideoPlayer(videoUrl: String) {
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)

            // Add error listener
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    // Show error banner when ExoPlayer encounters an error
                    showVideoError(error)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            // Optional: Show loading indicator
                        }
                        Player.STATE_READY -> {
                            // Video ready to play
                            hideVideoError()
                        }
                        Player.STATE_ENDED -> {
                            // Video playback ended
                        }
                        Player.STATE_IDLE -> {
                            // Player idle
                        }
                    }
                }
            })

            prepare()
            playWhenReady = false
        }

        binding.playerView.player = exoPlayer
    }

    private fun showVideoError(error: PlaybackException) {
        val errorMessage = when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                "Network connection failed. Please check your internet."
            }
            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> {
                "Failed to load video. Server returned an error."
            }
            PlaybackException.ERROR_CODE_UNSPECIFIED,
            PlaybackException.ERROR_CODE_REMOTE_ERROR -> {
                "Unable to load video. Trailer may be unavailable."
            }
            PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> {
                "Video stream interrupted. Please retry."
            }
            PlaybackException.ERROR_CODE_TIMEOUT -> {
                "Video loading timeout. Please retry."
            }
            PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE,
            PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED,
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> {
                "Invalid video format or corrupted file."
            }
            else -> {
                "Video playback error. Unable to play trailer."
            }
        }


        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
            .show()


        binding.playerView.visibility = View.GONE
        binding.ivAnimeHero.visibility = View.VISIBLE
    }

    private fun hideVideoError() {
        // Clear any error states if needed
        // Make sure player is visible if it was hidden
        if (exoPlayer != null) {
            binding.playerView.visibility = View.VISIBLE
            binding.ivAnimeHero.visibility = View.GONE
        }
    }

    private fun toggleSynopsis() {
        isExpanded = !isExpanded
        binding.tvSynopsis.maxLines = if (isExpanded) Int.MAX_VALUE else 3
        binding.btnReadMore.text = if (isExpanded) "Read Less" else "Read More"
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentGroup.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
        binding.contentGroup.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }
}
