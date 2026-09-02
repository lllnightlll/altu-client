package com.example.altu.SoundBar

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MusicController(
    context: Context,
    private val playlist: List<Track> = Playlist.tracks,
) {
    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())
    private var player: MediaPlayer? = null

    var trackIndex by mutableIntStateOf(0)
        private set
    var isPlaying by mutableStateOf(false)
        private set
    var isSeeking by mutableStateOf(false)
    var progress by mutableFloatStateOf(0f)

    val currentTrack: Track
        get() = playlist[trackIndex.coerceIn(0, playlist.lastIndex)]

    init {
        prepareCurrentTrack(autoPlay = false)
    }

    fun togglePlayPause() {
        if (isPlaying) pause() else play()
    }

    fun play() {
        val mediaPlayer = player ?: prepareCurrentTrack(autoPlay = true) ?: return
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
        isPlaying = true
    }

    fun pause() {
        player?.takeIf { it.isPlaying }?.pause()
        isPlaying = false
    }

    fun playNext(autoPlay: Boolean = isPlaying) {
        goToTrack(trackIndex + 1, autoPlay = autoPlay)
    }

    fun playPrevious() {
        val mediaPlayer = player
        if (mediaPlayer != null && mediaPlayer.currentPosition > 3000) {
            seekToFraction(0f)
        } else {
            goToTrack(trackIndex - 1, autoPlay = isPlaying)
        }
    }

    fun seekToFraction(fraction: Float) {
        val mediaPlayer = player ?: return
        val duration = mediaPlayer.duration
        if (duration <= 0) return
        mediaPlayer.seekTo((fraction.coerceIn(0f, 1f) * duration).toInt())
        progress = fraction
    }

    fun syncProgress() {
        val mediaPlayer = player ?: return
        if (isSeeking) return
        val duration = mediaPlayer.duration
        if (duration > 0) {
            progress = mediaPlayer.currentPosition.toFloat() / duration
        }
    }

    fun release() {
        player?.setOnCompletionListener(null)
        player?.release()
        player = null
        isPlaying = false
    }

    private fun goToTrack(index: Int, autoPlay: Boolean) {
        val nextIndex = ((index % playlist.size) + playlist.size) % playlist.size
        trackIndex = nextIndex
        progress = 0f
        prepareCurrentTrack(autoPlay = autoPlay)
    }

    private fun prepareCurrentTrack(autoPlay: Boolean): MediaPlayer? {
        player?.setOnCompletionListener(null)
        player?.release()
        player = null

        val mediaPlayer = MediaPlayer.create(appContext, currentTrack.trackRes)?.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            isLooping = false
            setOnCompletionListener {
                mainHandler.post {
                    playNext(autoPlay = true)
                }
            }
        }
        player = mediaPlayer
        if (autoPlay && mediaPlayer != null) {
            mediaPlayer.start()
            isPlaying = true
        } else if (!autoPlay) {
            isPlaying = false
        }
        return mediaPlayer
    }
}
