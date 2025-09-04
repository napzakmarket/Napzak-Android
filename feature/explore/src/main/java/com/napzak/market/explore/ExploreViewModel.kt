package com.napzak.market.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.napzak.market.common.state.UiState
import com.napzak.market.common.type.BottomSheetType
import com.napzak.market.common.type.SortType
import com.napzak.market.common.type.TradeType
import com.napzak.market.explore.state.ExploreBottomSheetState
import com.napzak.market.explore.state.ExploreProducts
import com.napzak.market.explore.state.ExploreUiState
import com.napzak.market.genre.model.Genre
import com.napzak.market.genre.model.extractGenreIds
import com.napzak.market.genre.repository.GenreNameRepository
import com.napzak.market.interest.usecase.SetInterestProductUseCase
import com.napzak.market.mixpanel.MixpanelConstants.APPLIED_ARRAY_FILTER
import com.napzak.market.mixpanel.MixpanelConstants.APPLIED_GENRE_FILTER
import com.napzak.market.mixpanel.MixpanelConstants.FILTER_COUNT
import com.napzak.market.mixpanel.MixpanelConstants.FOR_SALE
import com.napzak.market.mixpanel.MixpanelConstants.HIGH_PRICE
import com.napzak.market.mixpanel.MixpanelConstants.LATEST
import com.napzak.market.mixpanel.MixpanelConstants.LOW_PRICE
import com.napzak.market.mixpanel.MixpanelConstants.POPULAR
import com.napzak.market.mixpanel.MixpanelConstants.POST_ID
import com.napzak.market.mixpanel.MixpanelConstants.POST_TYPE
import com.napzak.market.mixpanel.MixpanelConstants.SORT
import com.napzak.market.mixpanel.MixpanelConstants.TAB
import com.napzak.market.mixpanel.MixpanelConstants.VIEWED_EXPLORE
import com.napzak.market.mixpanel.MixpanelConstants.VIEWED_PRODUCT
import com.napzak.market.mixpanel.MixpanelConstants.WANTED
import com.napzak.market.mixpanel.trackEvent
import com.napzak.market.product.model.ExploreParameters
import com.napzak.market.product.model.Product
import com.napzak.market.product.model.SearchParameters
import com.napzak.market.product.repository.ProductExploreRepository
import com.napzak.market.ui_util.groupBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ExploreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productExploreRepository: ProductExploreRepository,
    private val genreNameRepository: GenreNameRepository,
    private val setInterestProductUseCase: SetInterestProductUseCase,
    private val mixpanel: MixpanelAPI?,
) : ViewModel() {
    val searchTerm = savedStateHandle.get<String>(SEARCH_TERM_KEY)
    val sortType = savedStateHandle.get<SortType>(SORT_TYPE_KEY)
    val tradeType = savedStateHandle.get<TradeType>(TRADE_TYPE_KEY)

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState = _uiState.asStateFlow()
    private val _bottomSheetState: MutableStateFlow<ExploreBottomSheetState> =
        MutableStateFlow(ExploreBottomSheetState())
    val bottomSheetState: StateFlow<ExploreBottomSheetState> = _bottomSheetState.asStateFlow()

    private val _genreSearchTerm = MutableStateFlow("")
    val genreSearchTerm = _genreSearchTerm.asStateFlow()

    private val _sideEffect = Channel<ExploreSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private var lastSuccessfulLoadedProducts = 0 to emptyList<Product>()
    private val interestDebounceFlow = MutableSharedFlow<Pair<Long, Boolean>>()

    init {
        if (sortType != null) updateSortOption(sortType)
        if (tradeType != null) updateTradeType(tradeType)
        updateGenreItemsInBottomSheet()
        handleInterestDebounce()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun handleInterestDebounce() = viewModelScope.launch {
        interestDebounceFlow
            .groupBy { it.first }
            .flatMapMerge { (_, flow) -> flow.debounce(DEBOUNCE_DELAY) }
            .collect { (productId, finalState) ->
                val originalState = lastSuccessfulLoadedProducts.second
                    .firstOrNull { it.productId == productId }
                    ?.isInterested

                if (originalState != finalState) return@collect //원래 값과 동일한 값이 되면 api 호출 생략

                setInterestProductUseCase(productId, finalState)
                    .onSuccess { updateExploreInformation() }
                    .onFailure {
                        Timber.e(it.message.toString())
                        updateLoadState(
                            UiState.Success(
                                ExploreProducts(
                                    productCount = lastSuccessfulLoadedProducts.first,
                                    productList = lastSuccessfulLoadedProducts.second,
                                )
                            )
                        )
                    }
            }
    }

    fun updateExploreInformation() = viewModelScope.launch {
        val parameters = with(uiState.value) {
            if (searchTerm.isNullOrEmpty()) {
                ExploreParameters(
                    sort = sortOption.toString(),
                    genreIds = filteredGenres.extractGenreIds(),
                    isOnSale = isSoldOutSelected,
                    isUnopened = isUnopenSelected,
                    cursor = null, // TODO: 추후 cursor 값 변경
                )
            } else {
                SearchParameters(
                    searchWord = searchTerm,
                    sort = sortOption.toString(),
                    genreIds = filteredGenres.extractGenreIds(),
                    isOnSale = isSoldOutSelected,
                    isUnopened = isUnopenSelected,
                    cursor = null, // TODO: 추후 cursor 값 변경
                )
            }
        }

        when (uiState.value.selectedTab) {
            TradeType.BUY -> {
                if (searchTerm.isNullOrEmpty()) {
                    productExploreRepository.getExploreBuyProducts(parameters as ExploreParameters)
                        .onSuccess {
                            updateLoadState(
                                UiState.Success(
                                    ExploreProducts(
                                        it.first,
                                        it.second
                                    )
                                )
                            )
                            lastSuccessfulLoadedProducts = it
                        }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                } else {
                    productExploreRepository.getSearchBuyProducts(parameters as SearchParameters)
                        .onSuccess {
                            updateLoadState(
                                UiState.Success(
                                    ExploreProducts(
                                        it.first,
                                        it.second
                                    )
                                )
                            )
                            lastSuccessfulLoadedProducts = it
                        }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                }
            }

            TradeType.SELL -> {
                if (searchTerm.isNullOrEmpty()) {
                    productExploreRepository.getExploreSellProducts(parameters as ExploreParameters)
                        .onSuccess {
                            updateLoadState(
                                UiState.Success(
                                    ExploreProducts(
                                        it.first,
                                        it.second
                                    )
                                )
                            )
                            lastSuccessfulLoadedProducts = it
                        }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                } else {
                    productExploreRepository.getSearchSellProducts(parameters as SearchParameters)
                        .onSuccess {
                            updateLoadState(
                                UiState.Success(
                                    ExploreProducts(
                                        it.first,
                                        it.second
                                    )
                                )
                            )
                            lastSuccessfulLoadedProducts = it
                        }
                        .onFailure { updateLoadState(UiState.Failure(it.message.toString())) }
                }
            }

            else -> {}
        }
    }


    fun updateTradeType(newTradeType: TradeType) {
        val props = mapOf(TAB to if (newTradeType == TradeType.SELL) FOR_SALE else WANTED)
        mixpanel?.trackEvent(VIEWED_EXPLORE, props)

        _uiState.update { currentState -> currentState.copy(selectedTab = newTradeType) }
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

            else -> {}
        }
    }

    private fun updateGenreItemsInBottomSheet() = viewModelScope.launch {
        genreNameRepository.getGenreNames(cursor = null, size = INIT_GENRE_LIST_SIZE)
            .onSuccess { genres ->
                _uiState.update { currentState ->
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
                    _uiState.value.initGenreItems
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

                _uiState.update { currentState ->
                    currentState.copy(genreSearchResultItems = genreList)
                }
            }
    }

    fun updateSelectedGenres(newGenres: List<Genre>) {
        val props = mapOf(
            FILTER_COUNT to newGenres.size,
            TAB to if (uiState.value.selectedTab == TradeType.SELL) FOR_SALE else WANTED,
        )
        mixpanel?.trackEvent(APPLIED_GENRE_FILTER, props)

        _uiState.update { currentState ->
            currentState.copy(
                filteredGenres = newGenres,
            )
        }
    }

    fun updateUnopenFilter() {
        _uiState.update { currentState ->
            currentState.copy(
                isUnopenSelected = !_uiState.value.isUnopenSelected,
            )
        }
    }

    fun updateSoldOutFilter() {
        _uiState.update { currentState ->
            currentState.copy(
                isSoldOutSelected = !_uiState.value.isSoldOutSelected,
            )
        }
    }

    fun updateSortOption(newSortOption: SortType) {
        val props = mapOf(
            SORT to when (uiState.value.sortOption) {
                SortType.RECENT -> LATEST
                SortType.POPULAR -> POPULAR
                SortType.HIGH_PRICE -> HIGH_PRICE
                SortType.LOW_PRICE -> LOW_PRICE
            },
            TAB to if (uiState.value.selectedTab == TradeType.SELL) FOR_SALE else WANTED,
        )
        mixpanel?.trackEvent(APPLIED_ARRAY_FILTER, props)

        _uiState.update { currentState ->
            currentState.copy(
                sortOption = newSortOption,
            )
        }
    }

    fun updateProductIsInterested(productId: Long, isInterested: Boolean) = viewModelScope.launch {
        when (val state = uiState.value.loadState) {
            is UiState.Success -> {
                val updatedProducts = state.data.productList.map { product ->
                    if (product.productId == productId) {
                        interestDebounceFlow.emit(productId to isInterested)
                        product.copy(isInterested = !product.isInterested)
                    } else {
                        product
                    }
                }

                val newState = ExploreProducts(state.data.productCount, updatedProducts)
                updateLoadState(UiState.Success(newState))

                when (isInterested) {
                    true -> _sideEffect.send(ExploreSideEffect.CancelToast)
                    false -> _sideEffect.send(ExploreSideEffect.ShowHeartToast)
                }
            }

            else -> {}
        }
    }

    private fun updateLoadState(loadState: UiState<ExploreProducts>) =
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState,
            )
        }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val INIT_GENRE_LIST_SIZE = 39
        private const val SEARCH_TERM_KEY = "searchTerm"
        private const val SORT_TYPE_KEY = "sortType"
        private const val TRADE_TYPE_KEY = "tradeType"
    }
}
