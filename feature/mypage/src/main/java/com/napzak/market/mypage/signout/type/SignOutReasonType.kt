package com.napzak.market.mypage.signout.type

enum class SignOutReasonType(
    val reason: String,
) {
    HARD_TO_FIND("원하는 굿즈를 찾기 어려워요"),
    HARD_TO_SELL("상품이 잘 안팔려요"),
    HARD_TO_USE("앱이 사용하기 불편해요"),
    HARMFUL_USER("비매너 사용자를 만났어요"),
    NEW_MARKET("새 마켓(계정)을 만들고 싶어요"),
    PRIVACY("개인정보 보호가 걱정돼요"),
    NO_LONGER_ACTIVE("더 이상 덕질 활동을 하지 않아요"),
    OTHER("다른 이유가 있어요");
}