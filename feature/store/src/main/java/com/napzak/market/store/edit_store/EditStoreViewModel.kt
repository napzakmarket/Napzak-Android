package com.napzak.market.store.edit_store

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.store.edit_store.state.EditStoreUiState
import com.napzak.market.store.model.StoreEditGenre
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.util.android.getHttpExceptionMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditStoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storeRepository: StoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditStoreUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun getEditProfile() {
        storeRepository.fetchEditProfile()
            .onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(
                        loadState = UiState.Success(Unit),
                        storeDetail = it
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(loadState = UiState.Failure(it.toString()))
                }
            }
    }

    fun checkNicknameValidity() = viewModelScope.launch {
        storeRepository.getValidateNickname(_uiState.value.storeDetail.nickname)
            .onFailure { exception ->
                UiState.Failure(exception.getHttpExceptionMessage() ?: "")
            }
    }

    fun saveEditedProfile() = viewModelScope.launch {
        //storeRepository.updateEditProfile(_uiState.value.storeDetail)
        Log.d("EditStoreViewModel", "must call storeRepository.updateEditProfile()")
    }

    fun updateStoreDetail(
        coverUrl: String = _uiState.value.storeDetail.coverUrl,
        photoUrl: String = _uiState.value.storeDetail.photoUrl,
        name: String = _uiState.value.storeDetail.nickname,
        description: String = _uiState.value.storeDetail.description,
        genres: List<StoreEditGenre> = _uiState.value.storeDetail.preferredGenres
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                storeDetail = currentState.storeDetail.copy(
                    coverUrl = coverUrl,
                    photoUrl = photoUrl,
                    nickname = name,
                    description = description,
                    preferredGenres = genres
                )
            )
        }
    }
}