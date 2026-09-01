package com.example.altu.ChatBar

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import kotlinx.coroutines.launch

private const val SCROLL_TO_TOP_THRESHOLD = 8

@Composable
fun ChatBar(
    chats: List<ChatItem> = ChatItems.items,
    onChatClick: (ChatItem) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= SCROLL_TO_TOP_THRESHOLD }
    }
    val accent = Color(0xFFACADAC)

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(chats, key = { it.id }) { chat ->
                ChatRow(
                    chat = chat,
                    onClick = { onChatClick(chat) },
                )
            }
        }

        AnimatedVisibility(
            visible = showScrollToTop,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .randomShadow()
                    .steppedBorder(width = 1.dp, color = accent, shape = RoundedCornerShape(22.dp))
                    .clip(CircleShape)
                    .background(Color(0xFF070809)),
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Scroll to top",
                    tint = accent,
                )
            }
        }
    }
}
