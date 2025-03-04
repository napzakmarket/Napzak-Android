package com.napzak.market.dummy.mapper

import com.napzak.market.dummy.dto.GetUserListResponse
import com.napzak.market.dummy.model.DummyUser

fun GetUserListResponse.UserData.toDummyUser() = DummyUser(
    id = this.id ?: 0,
    email = this.email.orEmpty(),
    firstName = this.firstName.orEmpty(),
    lastName = this.lastName.orEmpty(),
    profileImage = this.profileImage.orEmpty(),
)