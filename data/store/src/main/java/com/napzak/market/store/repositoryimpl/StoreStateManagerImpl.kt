package com.napzak.market.store.repositoryimpl

import com.napzak.market.util.android.StoreStateManager
import javax.inject.Inject

class StoreStateManagerImpl @Inject constructor() : StoreStateManager {
    private var isDeleting = false

    override fun setIsDeleting(value: Boolean) {
        isDeleting = value
    }

    override fun isDeleting(): Boolean = isDeleting
}