package com.napzak.market.store.edit_store.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_edit_hint_introduction
import com.napzak.market.feature.store.R.string.store_edit_sub_title_introduction
import com.napzak.market.feature.store.R.string.store_edit_title_introduction
import com.napzak.market.ui_util.bringIntoView

private const val DESCRIPTION_MAX_LENGTH = 200

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EditStoreDescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    EditStoreProfileContainer(
        title = stringResource(store_edit_title_introduction),
        subtitle = stringResource(store_edit_sub_title_introduction),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .bringIntoViewRequester(bringIntoViewRequester),
        ) {
            NapzakDefaultTextField(
                text = description,
                onTextChange = onDescriptionChange,
                hint = stringResource(store_edit_hint_introduction),
                textStyle = NapzakMarketTheme.typography.caption12sb,
                hintTextStyle = NapzakMarketTheme.typography.caption12m,
                verticalAlignment = Alignment.Top,
                contentAlignment = Alignment.TopStart,
                isSingleLined = false,
                modifier = Modifier
                    .onFocusEvent { bringIntoViewRequester.bringIntoView(coroutineScope, it) }
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        NapzakMarketTheme.colors.gray50,
                        RoundedCornerShape(14.dp),
                    )
                    .padding(
                        horizontal = 14.dp,
                        vertical = 16.dp,
                    ),
            )

            Spacer(Modifier.height(14.dp))

            DescriptionLengthText(
                description = description,
            )
        }
    }
}

@Composable
private fun DescriptionLengthText(
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "${description.length}/$DESCRIPTION_MAX_LENGTH",
            style = NapzakMarketTheme.typography.caption10r.copy(
                color = NapzakMarketTheme.colors.gray300,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditDescriptionSectionPreview() {
    NapzakMarketTheme {
        Column {
            EditStoreDescriptionSection(
                description = "",
                onDescriptionChange = {},
            )
        }
    }
}