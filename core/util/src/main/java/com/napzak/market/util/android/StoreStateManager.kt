package com.napzak.market.util.android

interface StoreStateManager {
    fun setIsDeleting(value: Boolean)
    fun isDeleting(): Boolean
}