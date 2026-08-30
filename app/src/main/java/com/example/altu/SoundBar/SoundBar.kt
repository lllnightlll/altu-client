package com.example.altu.SoundBar

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
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
import com.example.altu.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundBar(
    title: String = "The Crowds",
    @DrawableRes coverRes: Int = R.drawable.sound_icon,
    @RawRes trackRes: Int = R.raw.the_crowds,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var isSeeking by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    val player = remember(context, trackRes) {
        MediaPlayer.create(context, trackRes)?.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }
    }

    DisposableEffect(player) {
        player?.setOnCompletionListener {
            isPlaying = false
            progress = 0f
            player.seekTo(0)
        }
        onDispose {
            player?.setOnCompletionListener(null)
            if (player?.isPlaying == true) {
                player.stop()
            }
            player?.release()
        }
    }

    LaunchedEffect(isPlaying, player) {
        val mediaPlayer = player ?: return@LaunchedEffect
        if (isPlaying) {
            mediaPlayer.start()
            while (isPlaying) {
                val duration = mediaPlayer.duration
                if (!isSeeking && duration > 0) {
                    progress = mediaPlayer.currentPosition.toFloat() / duration
                }
                delay(80)
                if (!mediaPlayer.isPlaying) break
            }
        } else if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
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
                painter = painterResource(coverRes),
                contentDescription = title,
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
                text = title,
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
                onClick = { seekToFraction(0f) },
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
                onClick = { seekToFraction(0f) },
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
