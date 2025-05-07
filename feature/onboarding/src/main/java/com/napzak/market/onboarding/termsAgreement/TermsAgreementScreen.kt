package com.napzak.market.onboarding.termsAgreement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.napzak.market.designsystem.R.drawable.ic_arrow_left
import com.napzak.market.designsystem.R.drawable.ic_arrow_right
import com.napzak.market.designsystem.R.drawable.ic_first_step_indicator
import com.napzak.market.designsystem.component.button.NapzakButton
import com.napzak.market.designsystem.component.button.NapzakCheckedButton
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.onboarding.R.string.onboarding_all_agreement_title
import com.napzak.market.feature.onboarding.R.string.onboarding_next
import com.napzak.market.feature.onboarding.R.string.onboarding_title
import com.napzak.market.onboarding.termsAgreement.model.TermType
import com.napzak.market.onboarding.termsAgreement.model.TermsAgreementUiState
import com.napzak.market.onboarding.termsAgreement.model.getDisplayLabel
import com.napzak.market.util.android.noRippleClickable
import com.napzak.market.util.common.openUrl

@Composable
fun TermsAgreementRoute(
    viewModel: TermsAgreementViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    TermsAgreementScreen(
        uiState = viewModel.uiState.collectAsState().value,
        onAllAgreementClick = viewModel::onAllAgreementClick,
        onAgreementClick = viewModel::onAgreementClick,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
    )
}

@Composable
fun TermsAgreementScreen(
    uiState: TermsAgreementUiState,
    onAllAgreementClick: () -> Unit,
    onAgreementClick: (TermType) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NapzakMarketTheme.colors.white)
            .padding(horizontal = 20.dp, vertical = 40.dp),
    ) {
        TermsAgreementTopBar(
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(onboarding_title),
            style = NapzakMarketTheme.typography.title20b,
            color = NapzakMarketTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.height(24.dp))

        NapzakCheckedButton(
            checked = uiState.isAllAgreed,
            text = stringResource(onboarding_all_agreement_title),
            backgroundColor = NapzakMarketTheme.colors.gray50,
            onCheckedChange = { onAllAgreementClick() },
            modifier = Modifier
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            TermType.entries.forEach { term ->
                NapzakCheckedButton(
                    checked = uiState.isChecked(term),
                    text = term.getDisplayLabel(),
                    onCheckedChange = { onAgreementClick(term) },
                    onIconClick = { context.openUrl(term.detailContentUrl) },
                    icon = ImageVector.vectorResource(ic_arrow_right),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NapzakButton(
            text = stringResource(onboarding_next),
            onClick = onNextClick,
            enabled = uiState.isAllAgreed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        )
    }
}

@Composable
private fun TermsAgreementTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(ic_arrow_left),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .noRippleClickable(onClick = onBackClick)
                .size(12.dp),
        )

        Icon(
            imageVector = ImageVector.vectorResource(ic_first_step_indicator),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NapzakCheckItemPreview() {
    val fakeUiState = TermsAgreementUiState(
        agreedTerms = setOf(TermType.SERVICE_TERMS)
    )

    NapzakMarketTheme {
        TermsAgreementScreen(
            uiState = fakeUiState,
            onAllAgreementClick = {},
            onAgreementClick = {},
            onNextClick = {},
            onBackClick = {},
        )
    }
}