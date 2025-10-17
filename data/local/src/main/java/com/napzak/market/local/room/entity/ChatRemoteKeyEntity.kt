package com.napzak.market.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 채팅방 별로 채팅 기록의 최신 정보 및 페이지를 관리하기 위한 엔티티
 * @param roomId 채팅방 ID
 * @param refreshAnchorMessageId 메시지 최신화 기준
 * @param appendCursor 다음 페이지 호출을 위한 커서
 */
@Entity(tableName = "remote_keys")
data class ChatRemoteKeyEntity(
    @PrimaryKey val roomId: Long,
    val refreshAnchorMessageId: Long?,
    val appendCursor: String?,
)