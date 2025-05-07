package com.napzak.market.registration

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class RegistrationContract {
    @Immutable
    data class RegistrationUiState(
        val loadState: UiState<Nothing> = UiState.Loading,
        val imageUris: ImmutableList<Uri> = persistentListOf(),
        val genre: Genre? = null,
        val searchTerm: String = "",
        val genres: List<Genre> = emptyList(),
        val title: String = "",
        val description: String = "",
        val price: String = "",
    )

    sealed class RegistrationSideEffect {
        // TODO: API 연결 후 상세 페이지 연동 필요
        data class NavigateToDetail(val productId: Long) : RegistrationSideEffect()
    }
}
