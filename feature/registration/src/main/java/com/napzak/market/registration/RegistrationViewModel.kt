package com.napzak.market.registration

import androidx.lifecycle.ViewModel
import com.napzak.market.genre.model.Genre
import com.napzak.market.registration.RegistrationContract.RegistrationUiState
import com.napzak.market.registration.model.Photo
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
open class RegistrationViewModel @Inject constructor(
    // TODO: API 연결 (등록, 수정)
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState = _uiState.asStateFlow()

    fun updatePhotos(newImageUrlList: List<Photo>) = _uiState.update { currentState ->
        currentState.copy(
            imageUris = (currentState.imageUris + newImageUrlList).toImmutableList()
        )
    }

    fun deletePhoto(photoIndex: Int) = _uiState.update { currentState ->
        currentState.copy(
            imageUris = currentState.imageUris.filterIndexed { index, _ ->
                index != photoIndex
            }.toImmutableList()
        )
    }

    fun updateRepresentPhoto(newPhoto: Int) = _uiState.update { currentState ->
        val newImageUrlList = currentState.imageUris.toMutableList()

        newImageUrlList.add(0, newImageUrlList.removeAt(newPhoto))
        currentState.copy(
            imageUris = newImageUrlList.toImmutableList()
        )
    }

    fun updateGenre(genre: Genre) = _uiState.update { currentState ->
        currentState.copy(genre = genre)
    }

    fun updateTitle(title: String) = _uiState.update { currentState ->
        currentState.copy(title = title)
    }

    fun updateDescription(description: String) = _uiState.update { currentState ->
        currentState.copy(description = description)
    }

    fun updatePrice(price: String) = _uiState.update { currentState ->
        currentState.copy(price = price)
    }

    fun registerProduct() {
        // TODO: API 연결
    }

    fun editRegisteredProduct() {
        // TODO: API 연결
    }
}
