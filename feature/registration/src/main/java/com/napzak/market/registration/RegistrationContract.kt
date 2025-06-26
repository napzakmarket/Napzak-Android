package com.napzak.market.registration

import androidx.compose.runtime.Immutable
import com.napzak.market.common.state.UiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.registration.model.Photo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class RegistrationContract {
    @Immutable
    data class RegistrationUiState(
        val loadState: UiState<Unit> = UiState.Empty,
        val imageUris: ImmutableList<Photo> = persistentListOf(),
        val genre: Genre? = null,
        val title: String = "",
        val description: String = "",
        val price: String = "",
    )

    sealed class RegistrationSideEffect {
        data class NavigateToDetail(val productId: Long) : RegistrationSideEffect()
    }
}
