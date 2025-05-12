package com.napzak.market.store.edit_store

sealed class EditStoreSideEffect {
    data object OnEditComplete : EditStoreSideEffect()
}