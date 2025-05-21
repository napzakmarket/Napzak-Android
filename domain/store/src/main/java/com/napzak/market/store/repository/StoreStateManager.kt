package com.napzak.market.store.repository

interface StoreStateManager {
    fun setIsDeleting(value: Boolean)
    fun isDeleting(): Boolean
}