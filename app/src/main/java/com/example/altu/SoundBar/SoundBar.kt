package com.example.altu.SoundBar

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundBar(
    title: String = "Untitled",
    @DrawableRes coverRes: Int = R.drawable.sound_icon,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.25f) }

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
                onValueChange = { progress = it },
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
            IconButton(onClick = { }, modifier = Modifier.size(36.dp).randomShadow()) {
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
            IconButton(onClick = { }, modifier = Modifier.size(36.dp).randomShadow()) {
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