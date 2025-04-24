package com.napzak.market.detail.model

// TODO: 도메인으로 이동
data class ProductDetail(
    val productId: Long,
    val tradeType: String,
    val genreName: String,
    val productName: String,
    val price: Int,
    val uploadTime: String,
    val chatCount: Int,
    val interestCount: Int,
    val description: String,
    val productCondition: String,
    val standardDeliveryFee: Int,
    val halfDeliveryFee: Int,
    val isDeliveryIncluded: Boolean,
    val isPriceNegotiable: Boolean,
    val tradeStatus: String,
    val isOwnedByCurrentUser: Boolean,
    val isInterested: Boolean,
) {
    companion object {
        val mock = ProductDetail(
            productId = 1,
            tradeType = "SELL",
            genreName = "은혼",
            productName = "은혼 긴토키 히지카타 룩업은혼 긴토키 히지카",
            price = 125000,
            uploadTime = "1일전",
            chatCount = 1000,
            interestCount = 1000,
            description = "은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키" +
                    " 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 " +
                    "히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업아아아은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 " +
                    "히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타" +
                    " 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업은혼 긴토키 히지카타 룩업아아아카타",
            productCondition = "LIKE_NEW",
            standardDeliveryFee = 3600,
            halfDeliveryFee = 1800,
            isDeliveryIncluded = false,
            isPriceNegotiable = true,
            tradeStatus = "BEFORE_TRADE",
            isOwnedByCurrentUser = true,
            isInterested = false,
        )
    }
}



