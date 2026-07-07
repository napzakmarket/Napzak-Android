package com.napzak.market.event

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDetailSessionManager @Inject constructor() {
    private val _pendingProductId = MutableStateFlow<Long?>(null)
    val pendingProductId = _pendingProductId.asStateFlow()

    fun setPendingProductId(productId: Long?) {
        _pendingProductId.update { productId }
    }
}
