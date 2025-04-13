package com.napzak.market.registration.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.component.textfield.InputTextField
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.registration.R.string.product_description
import com.napzak.market.feature.registration.R.string.product_description_hint
import com.napzak.market.feature.registration.R.string.product_image
import com.napzak.market.feature.registration.R.string.product_image_description
import com.napzak.market.feature.registration.R.string.product_name
import com.napzak.market.feature.registration.R.string.product_name_hint
import com.napzak.market.util.android.noRippleClickable

private const val MAX_PRODUCT_NAME = 48
private const val MAX_PRODUCT_DESCRIPTION = 430

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RegistrationViewGroup(
    title: String,
    onCloseClick: () -> Unit,
    productImageUrls: List<String>,
    onPhotoClick: () -> Unit,
    onPhotoPress: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    productGenre: String,
    onGenreSelected: () -> Unit,
    productName: String,
    onProductNameChanged: (String) -> Unit,
    productDescription: String,
    onProductDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val paddedModifier = Modifier.padding(horizontal = 20.dp)
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = modifier
            .background(color = NapzakMarketTheme.colors.white)
            .noRippleClickable {
                focusManager.clearFocus()
            },
    ) {
        stickyHeader {
            RegistrationTopBar(
                title = title,
                onCloseClick = onCloseClick,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(product_image),
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.gray500,
                ),
                modifier = paddedModifier,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(product_image_description),
                style = NapzakMarketTheme.typography.caption12m.copy(
                    color = NapzakMarketTheme.colors.gray300,
                ),
                modifier = paddedModifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            PhotoPicker(
                imageUrlList = productImageUrls,
                onPhotoClick = onPhotoClick,
                onLongClick = onPhotoPress,
                onDeleteClick = onDeleteClick,
            )
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))

            RegistrationGenreButton(
                selectedGenre = productGenre,
                onGenreClick = onGenreSelected,
                modifier = paddedModifier
                    .padding(vertical = 8.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionDivider()
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(product_name),
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.gray500,
                ),
                modifier = paddedModifier,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = paddedModifier,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InputTextField(
                    text = productName,
                    onTextChange = onProductNameChanged,
                    hint = stringResource(product_name_hint),
                )
                Text(
                    text = "${productName.length}/$MAX_PRODUCT_NAME",
                    style = NapzakMarketTheme.typography.caption10r.copy(
                        color = NapzakMarketTheme.colors.gray300,
                    ),
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(product_description),
                style = NapzakMarketTheme.typography.body14b.copy(
                    color = NapzakMarketTheme.colors.gray500,
                ),
                modifier = paddedModifier,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = paddedModifier,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InputTextField(
                    text = productDescription,
                    onTextChange = onProductDescriptionChanged,
                    hint = stringResource(product_description_hint),
                    modifier = Modifier
                        .height(136.dp),
                    isSingleLined = false,
                    contentAlignment = Alignment.TopStart,
                )
                Text(
                    text = "${productDescription.length}/$MAX_PRODUCT_DESCRIPTION",
                    style = NapzakMarketTheme.typography.caption10r.copy(
                        color = NapzakMarketTheme.colors.gray300,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationViewGroupPreview() {
    NapzakMarketTheme {
        var name by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        RegistrationViewGroup(
            title = "팔아요 등록",
            onCloseClick = { },
            productImageUrls = listOf(),
            onPhotoClick = { },
            onPhotoPress = { },
            onDeleteClick = { },
            productGenre = "사카모토데이즈",
            onGenreSelected = { },
            productName = name,
            onProductNameChanged = { name = it },
            productDescription = content,
            onProductDescriptionChanged = { content = it },
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
