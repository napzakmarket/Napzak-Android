package com.napzak.market.store.store

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.MarketTab
import com.napzak.market.common.type.SortType
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.extractGenreIds
import com.napzak.market.genre.repository.GenreNameRepository
import com.napzak.market.interest.usecase.SetInterestProductUseCase
import com.napzak.market.product.model.Product
import com.napzak.market.product.usecase.GetStoreProductsUseCase
import com.napzak.market.store.model.StoreDetail
import com.napzak.market.store.repository.StoreRepository
import com.napzak.market.store.store.state.StoreBottomSheetState
import com.napzak.market.store.store.state.StoreOptionState
import com.napzak.market.store.store.state.StoreUiState
import com.napzak.market.ui_util.groupBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storeRepository: StoreRepository,
    private val getProductStoreUseCase: GetStoreProductsUseCase,
    private val genreNameRepository: GenreNameRepository,
    private val setInterestProductUseCase: SetInterestProductUseCase,
) : ViewModel() {
    val storeId: Long = savedStateHandle.get<Long>(STORE_ID_KEY) ?: 0

    private val _genreSearchTerm = MutableStateFlow("")
    private val _storeOptionState: MutableStateFlow<StoreOptionState> =
        MutableStateFlow(StoreOptionState())
    val storeOptionState = _storeOptionState.asStateFlow()

    private val _bottomSheetState: MutableStateFlow<StoreBottomSheetState> =
        MutableStateFlow(StoreBottomSheetState())
    val bottomSheetState: StateFlow<StoreBottomSheetState> = _bottomSheetState.asStateFlow()

    private val _storeDetailState = MutableStateFlow<UiState<StoreDetail>>(UiState.Loading)
    private val _storeProductsState =
        MutableStateFlow<UiState<Pair<Int, List<Product>>>>(UiState.Loading)
    val storeUiState: StateFlow<StoreUiState> = combine(
        _storeDetailState,
        _storeProductsState,
    ) { storeDetailState, storeProductsState ->
        StoreUiState(
            storeDetailState = storeDetailState,
            storeProductsState = storeProductsState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StoreUiState(
            storeDetailState = UiState.Loading,
            storeProductsState = UiState.Loading,
        )
    )

    private val _sideEffect = Channel<StoreSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val lastSuccessfulLoadedProducts = mutableStateOf(0 to emptyList<Product>())
    private val interestDebounceFlow = MutableSharedFlow<Pair<Long, Boolean>>()

    init {
        handleInterestDebounce()
    }

    fun getStoreInformation() {
        updateStoreDetail()
        updateStoreProducts()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun handleInterestDebounce() = viewModelScope.launch {
        interestDebounceFlow
            .groupBy { it.first }
            .flatMapMerge { (_, flow) -> flow.debounce(DEBOUNCE_DELAY) }
            .collect { (productId, finalState) ->
                val originalState = lastSuccessfulLoadedProducts.value.second
                    .firstOrNull { it.productId == productId }
                    ?.isInterested

                if (originalState != finalState) return@collect //원래 값과 동일한 값이 되면 api 호출 생략

                setInterestProductUseCase(productId, finalState)
                    .onSuccess { updateStoreProducts() }
                    .onFailure {
                        Timber.e(it.message.toString())
                        lastSuccessfulLoadedProducts.let { lastProducts ->
                            _storeProductsState.update {
                                UiState.Success(
                                    Pair(
                                        lastProducts.value.first,
                                        lastProducts.value.second,
                                    )
                                )
                            }
                        }
                    }
            }
    }

    fun updateStoreDetail() = viewModelScope.launch {
        storeRepository.fetchStoreDetail(storeId)
            .onSuccess { _storeDetailState.value = UiState.Success(it) }
            .onFailure { _storeDetailState.value = UiState.Failure(it.message.toString()) }
    }

    fun updateStoreProducts() = viewModelScope.launch {
        with(_storeOptionState.value) {
            val result = getProductStoreUseCase(
                storeId = storeId,
                isMarketTypeSell = selectedTab == MarketTab.SELL,
                filteredGenres = filteredGenres.extractGenreIds(),
                isOnSale = isOnSale,
                sortOption = sortOption.name,
            )

            result
                .onSuccess { (count, list) ->
                    _storeProductsState.value = UiState.Success(Pair(count, list))
                    lastSuccessfulLoadedProducts.value = count to list
                }
                .onFailure { _storeProductsState.value = UiState.Failure(it.message.toString()) }
        }
    }

    fun updateMarketTabType(newTradeType: MarketTab) {
        _storeOptionState.update { currentState ->
            currentState.copy(
                selectedTab = newTradeType,
            )
        }
    }

    fun updateBottomSheetVisibility(type: BottomSheetType) {
        when (type) {
            BottomSheetType.GENRE_SEARCHING -> {
                _bottomSheetState.update {
                    it.copy(isGenreSearchingBottomSheetVisible = !_bottomSheetState.value.isGenreSearchingBottomSheetVisible)
                }
            }

            BottomSheetType.SORT -> {
                _bottomSheetState.update {
                    it.copy(isSortBottomSheetVisible = !_bottomSheetState.value.isSortBottomSheetVisible)
                }
            }

            BottomSheetType.STORE_REPORT -> {
                _bottomSheetState.update {
                    it.copy(isStoreReportBottomSheetVisible = !_bottomSheetState.value.isStoreReportBottomSheetVisible)
                }
            }

            else -> {}
        }
    }

    fun updateGenreItemsInBottomSheet() = viewModelScope.launch {
        genreNameRepository.getGenreNames(cursor = null, size = INIT_GENRE_LIST_SIZE)
            .onSuccess { genres ->
                _storeOptionState.update { currentState ->
                    currentState.copy(
                        initGenreItems = genres.first,
                        genreSearchResultItems = genres.first,
                    )
                }
            }
            .onFailure(Timber::e)
    }

    fun updateGenreSearchTerm(searchTerm: String) {
        _genreSearchTerm.update { searchTerm }
    }

    @OptIn(FlowPreview::class)
    fun updateGenreSearchResult() = viewModelScope.launch {
        _genreSearchTerm
            .debounce(DEBOUNCE_DELAY)
            .collectLatest { searchTerm ->
                val genreList = if (searchTerm.isBlank()) {
                    _storeOptionState.value.initGenreItems
                } else {
                    genreNameRepository.getGenreNameResults(searchTerm)
                        .fold(
                            onSuccess = { it.genreList },
                            onFailure = {
                                Timber.e(it)
                                emptyList()
                            }
                        )
                }

                _storeOptionState.update { currentState ->
                    currentState.copy(genreSearchResultItems = genreList)
                }
            }
    }

    fun updateSelectedGenres(newGenres: List<Genre>) {
        _storeOptionState.update { currentState ->
            currentState.copy(
                filteredGenres = newGenres,
            )
        }
    }

    fun updateIsOnSale() {
        _storeOptionState.update { currentState ->
            currentState.copy(
                isOnSale = !_storeOptionState.value.isOnSale,
            )
        }
    }

    fun updateSortOption(newSortOption: SortType) {
        _storeOptionState.update { currentState ->
            currentState.copy(
                sortOption = newSortOption,
            )
        }
    }

    fun updateProductIsInterested(productId: Long, isInterested: Boolean) = viewModelScope.launch {
        val state = storeUiState.value.storeProductsState
        when (state) {
            is UiState.Success -> {
                val (count, products) = state.data
                val updatedProducts = products.map { product ->
                    if (product.productId == productId) {
                        interestDebounceFlow.emit(productId to isInterested)
                        product.copy(isInterested = !product.isInterested)
                    } else {
                        product
                    }
                }
                _storeProductsState.update { UiState.Success(Pair(count, updatedProducts)) }

                when (isInterested) {
                    true -> _sideEffect.send(StoreSideEffect.CancelToast)
                    false -> _sideEffect.send(StoreSideEffect.ShowHeartToast)
                }
            }

            else -> {}
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val INIT_GENRE_LIST_SIZE = 39
        private const val STORE_ID_KEY = "storeId"
    }
}
