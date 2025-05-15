package com.napzak.market.store.edit_store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.textfield.NapzakDefaultTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_edit_hint_introduction
import com.napzak.market.feature.store.R.string.store_edit_sub_title_introduction
import com.napzak.market.feature.store.R.string.store_edit_title_introduction

private const val DESCRIPTION_MAX_LENGTH = 200

@Composable
internal fun EditStoreDescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    val paddingValues = PaddingValues(horizontal = 20.dp)

    EditStoreProfileContainer(
        title = stringResource(store_edit_title_introduction),
        subtitle = stringResource(store_edit_sub_title_introduction),
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
                .padding(paddingValues)
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    NapzakMarketTheme.colors.gray50,
                    RoundedCornerShape(14.dp),
                )
                .padding(PaddingValues(14.dp, 16.dp, 14.dp, 16.dp)),
        )

        Spacer(Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
            Text(
                text = "${description.length}/$DESCRIPTION_MAX_LENGTH",
                style = NapzakMarketTheme.typography.caption10r.copy(
                    color = NapzakMarketTheme.colors.gray300,
                ),
            )
        }
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