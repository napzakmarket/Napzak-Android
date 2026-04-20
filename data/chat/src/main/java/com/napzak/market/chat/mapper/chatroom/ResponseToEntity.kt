package com.napzak.market.chat.mapper.chatroom

import com.napzak.market.chat.dto.ChatRoomInformationResponse
import com.napzak.market.chat.dto.ChatRoomListResponse
import com.napzak.market.local.room.entity.ChatProductEntity
import com.napzak.market.local.room.entity.ChatRoomEntity

internal fun ChatRoomListResponse.toEntities() = chatRooms.map { it.toEntity() }

internal fun ChatRoomListResponse.ChatRoom.toEntity() = ChatRoomEntity(
    roomId = roomId,
    opponentNickName = opponentNickname,
    opponentStorePhoto = opponentStorePhoto,
    lastMessage = lastMessage,
    lastMessageAt = lastMessageAt,
    unreadCount = unreadCount,
    isWithdrawn = isOpponentWithdrawn,
)

internal fun ChatRoomInformationResponse.toRoomEntity(roomId: Long): ChatRoomEntity =
    ChatRoomEntity(
        roomId = roomId,
        productId = productInfo.productId,
        opponentStoreId = storeInfo.storeId,
        opponentNickName = storeInfo.nickname,
        opponentStorePhoto = storeInfo.storePhoto,
        isWithdrawn = storeInfo.isWithdrawn,
        isReported = storeInfo.isReported,
        isOpponentStoreBlocked = storeInfo.isOpponentStoreBlocked,
        isChatBlocked = storeInfo.isChatBlocked,
    )


internal fun ChatRoomInformationResponse.toProductEntity() = with(productInfo) {
    ChatProductEntity(
        productId = productId,
        photo = photo,
        tradeType = tradeType,
        title = title,
        price = price,
        isPriceNegotiable = isPriceNegotiable,
        genreName = genreName,
        productOwnerId = productOwnerId,
        isMyProduct = isMyProduct,
        isProductDeleted = isProductDeleted,
    )
}