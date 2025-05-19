package com.napzak.market.store.edit_store.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.theme.NapzakMarketTheme

/**
 * 제목, 부제목, 내용을 포함하는 프로필 수정 화면의 기본 컴포넌트
 */
@Composable
internal fun EditStoreProfileContainer(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    val paddingValues = PaddingValues(horizontal = 20.dp)

    Text(
        text = title,
        style = NapzakMarketTheme.typography.body14sb.copy(
            color = NapzakMarketTheme.colors.gray400,
        ),
        modifier = Modifier.padding(paddingValues),
    )

    Spacer(Modifier.height(4.dp))

    Text(
        text = subtitle,
        style = NapzakMarketTheme.typography.caption10r.copy(
            color = NapzakMarketTheme.colors.gray300,
        ),
        modifier = Modifier.padding(paddingValues),
    )

    Spacer(Modifier.height(20.dp))

    content()
}