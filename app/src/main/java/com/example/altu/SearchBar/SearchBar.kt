package com.example.altu.SearchBar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

@Composable
fun SearchBar(
    query: String? = null,
    onQueryChange: (String) -> Unit = {},
    placeholder: String = "Search",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var innerQuery by remember { mutableStateOf("") }
    val text = query ?: innerQuery
    val windowShape = RoundedCornerShape(32.dp)
    val accent = Color(0xFFACADAC)

    Row(
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .randomShadow()
            //.border(1.dp, accent, windowShape)
            .steppedBorder(width = 1.dp, color = accent, shape = windowShape)
            .background(Color(0xFF070809), windowShape)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search",
            tint = accent,
            modifier = Modifier.size(22.dp)
        )
        BasicTextField(
            value = text,
            onValueChange = { value ->
                if (query == null) innerQuery = value
                onQueryChange(value)
            },
            singleLine = true,
            cursorBrush = SolidColor(accent),
            textStyle = TextStyle(color = accent, fontFamily = GothicFont, fontSize = 24.sp),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        placeholder,
                        color = accent.copy(alpha = 0.45f),
                        fontFamily = GothicFont,
                        fontSize = 24.sp,
                    )
                }
                innerTextField()
            }
        )
        GothicCross(color = accent)
        Spacer(modifier = Modifier.width(10.dp))
    }
}
