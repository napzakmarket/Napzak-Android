package com.napzak.market.dummy.state

import com.napzak.market.common.state.UiState
import com.napzak.market.dummy.model.DummyUser

data class DummyState(
    val dummyUsersLoadState: UiState<List<DummyUser>> = UiState.Empty,
)
