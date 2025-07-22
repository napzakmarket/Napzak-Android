package com.napzak.market.registration.repository

import androidx.core.net.toUri
import com.napzak.market.registration.datasource.RegistrationDataSource
import com.napzak.market.registration.mapper.toData
import com.napzak.market.registration.mapper.toDomain
import com.napzak.market.registration.model.PurchaseRegistrationProduct
import com.napzak.market.registration.model.SaleRegistrationProduct
import com.napzak.market.remote.ImageCompressor
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val registrationDataSource: RegistrationDataSource,
    private val imageCompressor: ImageCompressor,
) : RegistrationRepository {
    override suspend fun postSaleRegistration(
        saleRegistrationProduct: SaleRegistrationProduct,
    ): Result<Long> = runCatching {
        registrationDataSource
            .postSaleRegistration(saleRegistrationProduct.toData())
            .productId
    }

    override suspend fun getSaleRegistration(
        productId: Long,
    ): Result<SaleRegistrationProduct> = runCatching {
        registrationDataSource
            .getSaleRegistration(productId).toDomain()
    }

    override suspend fun putSaleRegistration(
        productId: Long,
        saleRegistrationProduct: SaleRegistrationProduct,
    ): Result<Unit> = runCatching {
        registrationDataSource
            .putSaleRegistration(productId, saleRegistrationProduct.toData())
    }

    override suspend fun postPurchaseRegistration(
        purchaseRegistrationProduct: PurchaseRegistrationProduct,
    ): Result<Long> = runCatching {
        registrationDataSource
            .postPurchaseRegistration(purchaseRegistrationProduct.toData())
            .productId
    }

    override suspend fun getPurchaseRegistration(
        productId: Long,
    ): Result<PurchaseRegistrationProduct> = runCatching {
        registrationDataSource.getPurchaseRegistration(productId).toDomain()
    }

    override suspend fun putPurchaseRegistration(
        productId: Long,
        purchaseRegistrationProduct: PurchaseRegistrationProduct,
    ): Result<Unit> = runCatching {
        registrationDataSource
            .putPurchaseRegistration(productId, purchaseRegistrationProduct.toData())
    }

    override suspend fun compressProductImage(
        imageUri: String,
    ): Result<String> = runCatching {
        imageCompressor.compressImage(imageUri.toUri())
    }.mapCatching { uri ->
        uri.toString()
    }

    override suspend fun clearCachedImage(): Result<Unit> = runCatching {
        imageCompressor.clearCachedImage()
    }
}
