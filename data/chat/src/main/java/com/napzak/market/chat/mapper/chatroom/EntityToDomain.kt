package com.napzak.market.chat.mapper.chatroom

import com.napzak.market.chat.model.ChatRoom
import com.napzak.market.chat.model.ChatRoomInformation
import com.napzak.market.chat.model.ProductBrief
import com.napzak.market.chat.model.StoreBrief
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.local.room.entity.ChatRoomEntity
import com.napzak.market.local.room.relation.ChatRoomWithProduct

internal fun ChatRoomEntity.toDomain() = ChatRoom(
    roomId = roomId,
    storeNickname = opponentNickName,
    storePhoto = opponentStorePhoto,
    lastMessage = lastMessage,
    unreadMessageCount = unreadCount,
    lastMessageAt = lastMessageAt,
    isOpponentWithdrawn = isWithdrawn,
)

internal fun ChatRoomWithProduct.toDomain(): ChatRoomInformation? {
    val productBrief = product?.toDomain() ?: return null
    return ChatRoomInformation(
        roomId = room.roomId,
        storeBrief = room.toStoreBrief(),
        productBrief = productBrief,
        isOpponentOnline = room.isOpponentOnline,
    )
}

private fun ChatRoomEntity.toStoreBrief() = StoreBrief(
    storeId = 0,
    nickname = opponentNickName,
    storePhoto = opponentStorePhoto,
    isWithdrawn = isWithdrawn,
    isReported = isReported,
    isOpponentStoreBlocked = isOpponentStoreBlocked,
    isMyStoreBlocked = isChatBlocked,
)

internal fun ChatProductEntity.toDomain(): ProductBrief = ProductBrief(
    productId = productId,
    tradeType = tradeType,
    title = title,
    price = price,
    genreName = genreName,
    isProductDeleted = isProductDeleted,
    isPriceNegotiable = isPriceNegotiable,
    photo = photo ?: "",
    productOwnerId = productOwnerId ?: 0L,
    isMyProduct = isMyProduct,
)