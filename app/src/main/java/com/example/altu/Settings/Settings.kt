package com.example.altu.Settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

@Composable
fun Settings(
    onBackToChats: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onDeleteAllMessages: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    val accent = Color(0xFFACADAC)
    val danger = Color(0xFFEC407A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        SettingsBar(onBackToChats = onBackToChats)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SettingsToggleRow(
                title = "Notifications",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it },
            )
            SettingsActionRow(
                title = "Delete all messages",
                onClick = onDeleteAllMessages,
            )
            SettingsActionRow(
                title = "Privacy",
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(22.dp),
                    )
                },
                onClick = onPrivacyClick,
            )
            SettingsActionRow(
                title = "Delete account",
                titleColor = danger,
                onClick = onDeleteAccount,
            )
        }
    }
}

@Composable
fun SettingsBar(
    onBackToChats: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val accent = Color(0xFFACADAC)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .randomShadow()
                .steppedBorder(
                    width = 1.dp,
                    color = accent,
                    shape = CircleShape,
                    fillColor = Color(0xFF070809),
                )
                .clickable(onClick = onBackToChats),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Chats",
                tint = accent,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val accent = Color(0xFFACADAC)
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .randomShadow()
            .steppedBorder(width = 1.dp, color = accent, shape = shape, fillColor = Color(0xFF070809))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = accent,
            fontFamily = GothicFont,
            fontSize = 22.sp,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFD4B8D8),
                checkedTrackColor = Color(0xFF4A2F52),
                uncheckedThumbColor = accent.copy(alpha = 0.7f),
                uncheckedTrackColor = Color(0xFF1C1E21),
                uncheckedBorderColor = accent.copy(alpha = 0.4f),
            ),
        )
    }
}

@Composable
private fun SettingsActionRow(
    title: String,
    titleColor: Color = Color(0xFFACADAC),
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val accent = Color(0xFFACADAC)
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .randomShadow()
            .steppedBorder(width = 1.dp, color = accent, shape = shape, fillColor = Color(0xFF070809))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = titleColor,
            fontFamily = GothicFont,
            fontSize = 22.sp,
            modifier = Modifier.weight(1f),
        )
        trailing?.invoke()
    }
}

@Composable
fun PrivacyScreen(
    onBack: () -> Unit = {},
) {
    val accent = Color(0xFFACADAC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        SettingsBar(onBackToChats = onBack)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .randomShadow()
                .steppedBorder(
                    width = 1.dp,
                    color = accent,
                    shape = RoundedCornerShape(16.dp),
                    fillColor = Color(0xFF070809),
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Privacy",
                color = accent,
                fontFamily = GothicFont,
                fontSize = 28.sp,
            )
            Text(
                text = "Messages in Altu are encrypted on your device and not stored on third-party servers without your consent.\n" + 
                        "You can delete your conversation or account at any time. Notifications can be disabled in the settings.\n" + 
                        "We do not sell data and do not pass it to third parties, except in cases permitted by law.",
                color = accent.copy(alpha = 0.8f),
                fontFamily = GothicFont,
                fontSize = 18.sp,
            )
        }
    }
}
