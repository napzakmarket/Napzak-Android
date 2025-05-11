package com.napzak.market.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_kakao_logo
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.login.R.string.login_kakao

@Composable
fun KakaoLoginButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NapzakMarketTheme.colors.kakaoYellow,
            contentColor = NapzakMarketTheme.colors.black,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(ic_kakao_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(12.dp),
            )

            Text(
                text = stringResource(login_kakao),
                style = NapzakMarketTheme.typography.body14b,
                color = NapzakMarketTheme.colors.black,
                modifier = Modifier
                    .padding(vertical = 8.dp),
            )
        }
    }
}