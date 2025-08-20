package com.napzak.market.chat.chatroom.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_gallery
import com.napzak.market.designsystem.R.drawable.ic_gray_send
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.chat.R.string.chat_room_input_field_gallery
import com.napzak.market.feature.chat.R.string.chat_room_input_field_hint
import com.napzak.market.feature.chat.R.string.chat_room_input_field_hint_withdrawn
import com.napzak.market.feature.chat.R.string.chat_room_input_field_send
import com.napzak.market.ui_util.ShadowDirection
import com.napzak.market.ui_util.napzakGradientShadow
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun ChatRoomInputField(
    text: String,
    enabled: Boolean,
    onTextChange: (String) -> Unit,
    onSendClick: (String) -> Unit,
    onPhotoSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { onPhotoSelect(it.toString()) } }

    val onGalleryClick = {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .napzakGradientShadow(
                height = 4.dp,
                startColor = NapzakMarketTheme.colors.transWhite,
                endColor = NapzakMarketTheme.colors.shadowBlack,
                direction = ShadowDirection.Top,
            )
            .padding(
                top = 8.dp,
                bottom = 14.dp,
                start = 20.dp,
                end = 20.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GalleryButton(
            enabled = enabled,
            onClick = onGalleryClick,
        )
        Spacer(modifier = Modifier.width(16.dp))
        ChatTextField(
            text = text,
            onTextChange = onTextChange,
            hint = stringResource(
                if (enabled) chat_room_input_field_hint
                else chat_room_input_field_hint_withdrawn
            ),
            enabled = enabled,
            modifier = Modifier.weight(1f),
            suffix = {
                SendButton(
                    enabled = text.isNotBlank() && enabled,
                    onClick = {
                        onSendClick(text)
                    },
                )
            },
        )
    }
}

@Composable
private fun ChatTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    enabled: Boolean,
    suffix: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    val backgroundColor = NapzakMarketTheme.colors.gray50
    val innerPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    val textStyle = NapzakMarketTheme.typography.body14r

    NapzakDefaultTextField(
        text = text,
        onTextChange = onTextChange,
        hint = hint,
        textStyle = textStyle,
        hintTextStyle = textStyle,
        suffix = suffix,
        enabled = enabled,
        isSingleLined = false,
        modifier = modifier
            .heightIn(max = 140.dp)
            .clip(shape)
            .background(color = backgroundColor)
            .padding(innerPadding),
    )
}

@Composable
private fun GalleryButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(ic_gallery),
        contentDescription = stringResource(chat_room_input_field_gallery),
        tint = NapzakMarketTheme.colors.gray200,
        modifier = modifier
            .then(
                if (enabled) Modifier.noRippleClickable(onClick = onClick)
                else Modifier
            )

    )
}

@Composable
private fun SendButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tint =
        if (enabled) NapzakMarketTheme.colors.purple500
        else NapzakMarketTheme.colors.gray200

    Icon(
        imageVector = ImageVector.vectorResource(ic_gray_send),
        contentDescription = stringResource(chat_room_input_field_send),
        tint = tint,
        modifier = modifier.noRippleClickable {
            if (enabled) onClick()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomInputFieldPreview() {
    NapzakMarketTheme {
        var textNotWithDrawn by remember { mutableStateOf("") }
        var textWithDrawn by remember { mutableStateOf("") }

        Column {
            ChatRoomInputField(
                text = textNotWithDrawn,
                onTextChange = { textNotWithDrawn = it },
                enabled = false,
                onSendClick = {},
                onPhotoSelect = {},
            )

            Spacer(Modifier.height(10.dp))

            ChatRoomInputField(
                text = textWithDrawn,
                onTextChange = { textWithDrawn = it },
                enabled = true,
                onSendClick = {},
                onPhotoSelect = {},
            )
        }
    }
}
