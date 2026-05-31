package com.napzak.market.search.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.napzak.market.navigation.AppNavigator
import com.napzak.market.navigation.EntryProviderBuilder
import com.napzak.market.navigation.keys.ExploreScreenKey
import com.napzak.market.navigation.keys.GenreDetailScreenKey
import com.napzak.market.navigation.keys.ScreenKey
import com.napzak.market.navigation.keys.SearchScreenKey
import com.napzak.market.search.SearchRoute
import javax.inject.Inject

class SearchEntryProvider @Inject constructor(
    private val navigator: AppNavigator,
) : EntryProviderBuilder {
    override fun EntryProviderScope<ScreenKey>.provide() {
        entry<SearchScreenKey> {
            SearchRoute(
                onBackButtonClick = navigator::pop,
                onSearchResultNavigate = ::navigateToSearchResult,
                onGenreDetailNavigate = ::navigateToGenreDetail,
            )
        }
    }

    private fun navigateToSearchResult(searchTerm: String) {
        navigator.pop()
        navigator.navigateTo(ExploreScreenKey(searchTerm = searchTerm))
    }

    private fun navigateToGenreDetail(genreId: Long) {
        navigator.navigateTo(GenreDetailScreenKey(genreId = genreId))
    }
}
