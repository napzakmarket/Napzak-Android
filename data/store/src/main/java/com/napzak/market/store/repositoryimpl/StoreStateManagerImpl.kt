package com.napzak.market.store.repositoryimpl

import com.napzak.market.util.android.StoreStateManager
import javax.inject.Inject

class StoreStateManagerImpl @Inject constructor() : StoreStateManager {
    private var isDeletingStore = false

    override fun setIsDeleting(value: Boolean) {
        isDeletingStore = value
    }

    override fun isDeletingStore(): Boolean = isDeletingStore
}