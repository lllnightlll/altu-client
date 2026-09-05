package com.example.altu.SoundBar

import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.altu.ui.theme.GothicFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundBar(
    controller: MusicController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val currentTrack = controller.currentTrack
    val isPlaying = controller.isPlaying
    val progress = controller.progress

    LaunchedEffect(isPlaying, controller) {
        if (!isPlaying) return@LaunchedEffect
        while (controller.isPlaying) {
            controller.syncProgress()
            delay(80)
        }
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
                fontFamily = GothicFont,
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.randomShadow()
            )
            Slider(
                value = progress,
                onValueChange = {
                    controller.isSeeking = true
                    controller.progress = it
                },
                onValueChangeFinished = {
                    controller.seekToFraction(controller.progress)
                    controller.isSeeking = false
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
                onClick = { controller.playPrevious() },
                modifier = Modifier.size(36.dp).randomShadow()
            ) {
                Icon(
                    Icons.Filled.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color(0xFFACADAC)
                )
            }
            IconButton(
                onClick = { controller.togglePlayPause() },
                modifier = Modifier.size(36.dp).randomShadow()
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color(0xFFb488a1)
                )
            }
            IconButton(
                onClick = { controller.playNext() },
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
