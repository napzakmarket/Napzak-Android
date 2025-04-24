package com.napzak.market.registration.purchase.state

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class PurchaseUiState (
    val loadState: UiState<Nothing> = UiState.Loading,
    val imageUris: ImmutableList<Uri> = persistentListOf(),
    val genre: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val isNegotiable: Boolean = false,
)
