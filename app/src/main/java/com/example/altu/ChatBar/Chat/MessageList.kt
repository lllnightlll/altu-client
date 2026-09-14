package com.example.altu.ChatBar.Chat

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import kotlinx.coroutines.launch

private const val SCROLL_TO_BOTTOM_THRESHOLD = 8

@Composable
fun MessageList(
    messages: List<Message>,
    searchQuery: String = "",
    findNextToken: Int = 0,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val accent = Color(0xFFACADAC)
    var focusedMessageId by remember { mutableStateOf<String?>(null) }

    val showScrollToBottom by remember(messages.size) {
        derivedStateOf {
            if (messages.isEmpty()) return@derivedStateOf false
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible < messages.lastIndex - SCROLL_TO_BOTTOM_THRESHOLD
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty() && searchQuery.isBlank()) {
            listState.scrollToItem(messages.lastIndex)
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            focusedMessageId = null
            return@LaunchedEffect
        }
        val from = listState.firstVisibleItemIndex
        val target = messages.nearestMatchIndex(searchQuery, from) ?: run {
            focusedMessageId = null
            return@LaunchedEffect
        }
        focusedMessageId = messages[target].id
        listState.animateScrollToItem(target)
    }

    LaunchedEffect(findNextToken) {
        if (findNextToken == 0 || searchQuery.isBlank()) return@LaunchedEffect
        val current = focusedMessageId
            ?.let { id -> messages.indexOfFirst { it.id == id } }
            ?.takeIf { it >= 0 }
            ?: listState.firstVisibleItemIndex
        val target = messages.nextMatchIndex(searchQuery, current) ?: return@LaunchedEffect
        focusedMessageId = messages[target].id
        listState.animateScrollToItem(target)
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(messages, key = { it.id }) { message ->
                MessageBubble(
                    message = message,
                    highlighted = message.id == focusedMessageId,
                    modifier = Modifier.padding(horizontal = 1.dp),
                )
            }
        }

        AnimatedVisibility(
            visible = showScrollToBottom,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(messages.lastIndex)
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .randomShadow()
                    .steppedBorder(
                        width = 1.dp,
                        color = accent,
                        shape = RoundedCornerShape(22.dp),
                        fillColor = Color(0xFF070809),
                    )
                    .clip(CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Scroll to bottom",
                    tint = accent,
                )
            }
        }
    }
}
