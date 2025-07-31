package com.napzak.market.presigned_url.model

data class UploadImage(
    val imageType: ImageType,
    val uri: String,
    val title: String = imageType.imageName,
    val presignedUrl: String = "",
) {
    enum class ImageType(
        val imageName: String = "",
    ) {
        PRODUCT,
        COVER(imageName = "cover.jpg"),
        PROFILE(imageName = "profile.jpg"),
        CHAT(imageName = "chat.jpg")
    }
}