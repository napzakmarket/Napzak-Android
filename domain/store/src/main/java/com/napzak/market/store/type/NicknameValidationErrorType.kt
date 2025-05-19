package com.napzak.market.store.type

enum class NicknameValidationErrorType(val message: String) {
    CONTAINS_WHITESPACE("띄어쓰기를 포함할 수 없어요."),
    CONTAINS_SPECIAL_CHAR("특수기호를 사용할 수 없어요."),
    CONTAINS_CONSONANT("초성만으로는 이름을 만들 수 없어요."),
    ONLY_NUMBERS("숫자만으로는 이름을 만들 수 없어요.\n한글이나 영문을 함께 사용해주세요."),
}