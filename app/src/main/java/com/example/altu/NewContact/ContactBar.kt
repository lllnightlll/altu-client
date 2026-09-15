package com.example.altu.NewContact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

enum class ContactTab {
    Qr,
    Scan,
}

@Composable
fun ContactBar(
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
fun ContactTabBar(
    selectedTab: ContactTab = ContactTab.Qr,
    onTabSelected: (ContactTab) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ContactTabButton(
            label = "QR",
            icon = Icons.Filled.QrCode,
            selected = selectedTab == ContactTab.Qr,
            onClick = { onTabSelected(ContactTab.Qr) },
            modifier = Modifier.weight(1f),
        )
        ContactTabButton(
            label = "Scan",
            icon = Icons.Filled.QrCodeScanner,
            selected = selectedTab == ContactTab.Scan,
            onClick = { onTabSelected(ContactTab.Scan) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ContactTabButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = if (selected) Color(0xFFD4B8D8) else Color(0xFFb488a1)
    val fill = if (selected) Color(0xFF1C1E21) else Color(0xFF070809)
    val shape = RoundedCornerShape(32.dp)

    Row(
        modifier = modifier
            .height(44.dp)
            .randomShadow()
            .steppedBorder(width = 1.dp, color = accent, shape = shape, fillColor = fill)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = accent,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = label,
            color = accent,
            fontFamily = GothicFont,
            fontSize = 22.sp,
        )
    }
}
