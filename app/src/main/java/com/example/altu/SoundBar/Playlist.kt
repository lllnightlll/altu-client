package com.example.altu.SoundBar

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.example.altu.R

data class Track(
    val title: String,
    @RawRes val trackRes: Int,
    @DrawableRes val coverRes: Int = R.drawable.sound_icon,
)

object Playlist {
    val tracks = listOf(
        Track(title = "The Crowds", trackRes = R.raw.the_crowds),
    )
}
