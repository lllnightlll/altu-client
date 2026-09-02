package com.example.altu.SoundBar

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundBar(
    tracks: List<Track> = Playlist.tracks,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val playlist = tracks.ifEmpty { Playlist.tracks }

    var trackIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var isSeeking by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var playerVersion by remember { mutableIntStateOf(0) }

    val currentTrack = playlist[trackIndex.coerceIn(0, playlist.lastIndex)]
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    val player = remember(context, currentTrack.trackRes, playerVersion) {
        MediaPlayer.create(context, currentTrack.trackRes)?.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            isLooping = false
        }
    }

    fun goToTrack(index: Int, autoPlay: Boolean) {
        val nextIndex = when {
            playlist.isEmpty() -> 0
            else -> ((index % playlist.size) + playlist.size) % playlist.size
        }
        progress = 0f
        trackIndex = nextIndex
        playerVersion++
        isPlaying = autoPlay
    }

    fun playNext(autoPlay: Boolean = isPlaying) {
        goToTrack(trackIndex + 1, autoPlay = autoPlay)
    }

    fun playPrevious() {
        val mediaPlayer = player
        if (mediaPlayer != null && mediaPlayer.currentPosition > 3000) {
            mediaPlayer.seekTo(0)
            progress = 0f
        } else {
            goToTrack(trackIndex - 1, autoPlay = isPlaying)
        }
    }

    DisposableEffect(player) {
        player?.setOnCompletionListener {
            mainHandler.post {
                playNext(autoPlay = true)
            }
        }
        onDispose {
            player?.setOnCompletionListener(null)
            if (player?.isPlaying == true) {
                player.pause()
            }
            player?.release()
        }
    }

    LaunchedEffect(isPlaying, player) {
        val mediaPlayer = player ?: return@LaunchedEffect
        if (!isPlaying) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            return@LaunchedEffect
        }

        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }

        while (isPlaying) {
            if (!mediaPlayer.isPlaying) {
                break
            }
            val duration = mediaPlayer.duration
            if (!isSeeking && duration > 0) {
                progress = mediaPlayer.currentPosition.toFloat() / duration
            }
            delay(80)
        }
    }

    fun seekToFraction(fraction: Float) {
        val mediaPlayer = player ?: return
        val duration = mediaPlayer.duration
        if (duration <= 0) return
        mediaPlayer.seekTo((fraction.coerceIn(0f, 1f) * duration).toInt())
        progress = fraction
    }

    Row(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(Color(0xFF070809))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1.25f)
                .randomShadow()
                .iconShape(edgeInset = 7.dp)
        ) {
            Image(
                painter = painterResource(currentTrack.coverRes),
                contentDescription = currentTrack.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(7.dp)
                    .fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentTrack.title,
                color = Color(0xFFACADAC),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.randomShadow()
            )
            Slider(
                value = progress,
                onValueChange = {
                    isSeeking = true
                    progress = it
                },
                onValueChangeFinished = {
                    seekToFraction(progress)
                    isSeeking = false
                },
                modifier = Modifier.randomShadow(),
                track = { state ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .randomShadow()
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color(0xFFACADAC))
                        )
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(state.coercedValueAsFraction)
                                .background(Color(0xFFACADAC))
                        )
                    }
                },
                thumb = {
                    CardiogramThumb(color = Color(0xFFACADAC))
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { playPrevious() },
                modifier = Modifier.size(36.dp).randomShadow()
            ) {
                Icon(
                    Icons.Filled.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color(0xFFACADAC)
                )
            }
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier.size(36.dp).randomShadow()
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color(0xFFb488a1)
                )
            }
            IconButton(
                onClick = { playNext(autoPlay = isPlaying) },
                modifier = Modifier.size(36.dp).randomShadow()
            ) {
                Icon(
                    Icons.Filled.SkipNext,
                    contentDescription = "Next",
                    tint = Color(0xFFACADAC)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}
