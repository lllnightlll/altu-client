package com.example.altu.ChatBar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatBar(
    chats: List<ChatItem> = ChatItems.items,
    onChatClick: (ChatItem) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        items(chats, key = { it.id }) { chat ->
            ChatRow(
                chat = chat,
                onClick = { onChatClick(chat) },
            )
        }
    }
}