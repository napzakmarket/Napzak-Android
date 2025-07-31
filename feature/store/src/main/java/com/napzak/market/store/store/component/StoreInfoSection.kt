package com.napzak.market.store.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.feature.store.R.string.store_edit_profile
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.ui_util.noRippleClickable

@Composable
internal fun StoreInfoSection(
    storeDetail: StoreDetail,
    isMyStore: Boolean,
    onProfileEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val profileImageShape = CircleShape

    with(storeDetail) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data(coverUrl)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2.25f)
                            .background(color = NapzakMarketTheme.colors.gray100),
                    )

                    if (isMyStore) {
                        Box(
                            modifier = Modifier
                                .padding(end = 20.dp, bottom = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(store_edit_profile),
                                style = NapzakMarketTheme.typography.caption10sb,
                                color = NapzakMarketTheme.colors.white,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .noRippleClickable(onProfileEditClick)
                                    .background(
                                        color = NapzakMarketTheme.colors.transBlack,
                                        shape = RoundedCornerShape(24.dp),
                                    )
                                    .padding(6.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(35.dp))
            }

            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(photoUrl)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(profileImageShape)
                    .background(color = NapzakMarketTheme.colors.gray200)
                    .border(
                        width = 5.dp,
                        color = NapzakMarketTheme.colors.white,
                        shape = profileImageShape,
                    ),
            )
        }

        Spacer(Modifier.height(9.dp))

        Text(
            text = nickname,
            style = NapzakMarketTheme.typography.body16sb,
            color = NapzakMarketTheme.colors.gray500,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = description,
            style = NapzakMarketTheme.typography.caption10sb,
            color = NapzakMarketTheme.colors.gray500,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
        )

        Spacer(Modifier.height(20.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 5.dp,
                alignment = Alignment.CenterHorizontally,
            )
        ) {
            items(genrePreferences) { genre ->
                GenreChip(
                    genreName = genre.genreName,
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
