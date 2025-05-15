package com.napzak.market.main.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.CommonSnackBar
import com.napzak.market.designsystem.theme.NapzakMarketTheme

@Composable
fun MainSnackBarHost(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    imageRes: Int? = null,
) {
    SnackbarHost(snackBarHostState) {
        CommonSnackBar(
            message = it.visuals.message,
            icon = imageRes?.let { res -> ImageVector.vectorResource(res) },
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 15.dp),
            textStyle = NapzakMarketTheme.typography.caption12m.copy(
                color = NapzakMarketTheme.colors.white,
                textAlign = TextAlign.Center,
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
        )
    }
}