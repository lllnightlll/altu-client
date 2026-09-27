package com.example.altu.Auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.ChatBar.HeartAvatar
import com.example.altu.R
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

@Composable
fun RegisterScreen(
    busy: Boolean = false,
    error: String? = null,
    onRegister: (tag: String, avatarUri: Uri?) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    var tag by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        avatarUri = uri
    }
    val accent = Color(0xFFACADAC)
    val ink = Color(0xFF070809)
    val fieldShape = RoundedCornerShape(32.dp)
    val fieldStyle = TextStyle(
        color = accent,
        fontFamily = GothicFont,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    )
    val canSubmit = TagRules.errorOrNull(tag) == null && !busy

    fun submit() {
        if (canSubmit) onRegister(TagRules.normalize(tag), avatarUri)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "altu",
            color = accent,
            fontFamily = GothicFont,
            fontSize = 48.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Register",
            color = accent.copy(alpha = 0.7f),
            fontFamily = GothicFont,
            fontSize = 26.sp,
        )
        Spacer(modifier = Modifier.height(24.dp))
        HeartAvatar(
            contentDescription = "Avatar",
            avatarRes = R.drawable.ic_launcher_foreground,
            avatarUri = avatarUri,
            height = 88.dp,
            modifier = Modifier.clickable(enabled = !busy) { picker.launch("image/*") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (avatarUri == null) "Add avatar" else "Change avatar",
            color = accent.copy(alpha = 0.7f),
            fontFamily = GothicFont,
            fontSize = 18.sp,
            modifier = Modifier.clickable(enabled = !busy) { picker.launch("image/*") },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .randomShadow()
                .steppedBorder(width = 1.dp, color = accent, shape = fieldShape, fillColor = ink)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = tag,
                onValueChange = { tag = it },
                singleLine = true,
                enabled = !busy,
                cursorBrush = SolidColor(accent),
                textStyle = fieldStyle,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (tag.isEmpty()) {
                            Text(
                                text = "tag",
                                style = fieldStyle.copy(color = accent.copy(alpha = 0.45f)),
                            )
                        }
                        inner()
                    }
                },
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = error ?: "3-24 letters, digits or _",
            color = if (error == null) accent.copy(alpha = 0.45f) else Color(0xFFEC407A),
            fontFamily = GothicFont,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .randomShadow()
                .steppedBorder(
                    width = 1.dp,
                    color = if (canSubmit) accent else accent.copy(alpha = 0.35f),
                    shape = fieldShape,
                    fillColor = ink,
                )
                .clickable(enabled = canSubmit, onClick = { submit() }),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (busy) "Saving" else "Create account",
                color = if (canSubmit) accent else accent.copy(alpha = 0.35f),
                fontFamily = GothicFont,
                fontSize = 24.sp,
            )
        }
    }
}
