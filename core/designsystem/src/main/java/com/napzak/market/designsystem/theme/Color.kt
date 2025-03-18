package com.napzak.market.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Primary
val purple500 = Color(0xFF7534FF)
val purple200 = Color(0xFFDFD0FF)
val purple100 = Color(0xFFF1EAFF)

// Gray Scale
val black = Color(0xFF2C2C2C)
val gray500 = Color(0xFF3B3B3B)
val gray400 = Color(0xFF545454)
val gray300 = Color(0xFF7F7F7F)
val gray200 = Color(0xFFBCBCBC)
val gray100 = Color(0xFFD9D9D9)
val gray50 = Color(0xFFF5F5F5)
val white = Color(0xFFFFFFFF)

// State
val red = Color(0xFFEF4849)
val green = Color(0xFF1BD368)

// Transparency
val purpleAlpha = Color(0xB37534FF)
val blackAlpha = Color(0xB31A1A1A)
val lightPurpleAlpha = Color(0xB3F1EAFF)

// Gradient
val whiteGrad = Color(0x00FFFFFF)

@Stable
class NapzakMarketColors(
    purple500: Color,
    purple200: Color,
    purple100: Color,
    black: Color,
    gray500: Color,
    gray400: Color,
    gray300: Color,
    gray200: Color,
    gray100: Color,
    gray50: Color,
    white: Color,
    red: Color,
    green: Color,
    purpleAlpha: Color,
    blackAlpha: Color,
    lightPurpleAlpha: Color,
    whiteGrad: Color,
    isLight: Boolean
) {
    var purple500 by mutableStateOf(purple500)
        private set
    var purple200 by mutableStateOf(purple200)
        private set
    var purple100 by mutableStateOf(purple100)
        private set
    var black by mutableStateOf(black)
        private set
    var gray500 by mutableStateOf(gray500)
        private set
    var gray400 by mutableStateOf(gray400)
        private set
    var gray300 by mutableStateOf(gray300)
        private set
    var gray200 by mutableStateOf(gray200)
        private set
    var gray100 by mutableStateOf(gray100)
        private set
    var gray50 by mutableStateOf(gray50)
        private set
    var white by mutableStateOf(white)
        private set
    var red by mutableStateOf(red)
        private set
    var green by mutableStateOf(green)
        private set
    var purpleAlpha by mutableStateOf(purpleAlpha)
        private set
    var blackAlpha by mutableStateOf(blackAlpha)
        private set
    var lightPurpleAlpha by mutableStateOf(lightPurpleAlpha)
        private set
    var whiteGrad by mutableStateOf(whiteGrad)
        private set
    var isLight by mutableStateOf(isLight)

    fun copy(): NapzakMarketColors = NapzakMarketColors(
        purple500,
        purple200,
        purple100,
        black,
        gray500,
        gray400,
        gray300,
        gray200,
        gray100,
        gray50,
        white,
        red,
        green,
        purpleAlpha,
        blackAlpha,
        lightPurpleAlpha,
        whiteGrad,
        isLight
    )

    fun update(other: NapzakMarketColors) {
        purple500 = other.purple500
        purple200 = other.purple200
        purple100 = other.purple100
        black = other.black
        gray500 = other.gray500
        gray400 = other.gray400
        gray300 = other.gray300
        gray200 = other.gray200
        gray100 = other.gray100
        gray50 = other.gray50
        white = other.white
        red = other.red
        green = other.green
        purpleAlpha = other.purpleAlpha
        blackAlpha = other.blackAlpha
        lightPurpleAlpha = other.lightPurpleAlpha
        whiteGrad = other.whiteGrad
        isLight = other.isLight
    }
}

fun NapzakMarketLightColors(
    Purple500: Color = purple500,
    Purple200: Color = purple200,
    Purple100: Color = purple100,
    Black: Color = black,
    Gray500: Color = gray500,
    Gray400: Color = gray400,
    Gray300: Color = gray300,
    Gray200: Color = gray200,
    Gray100: Color = gray100,
    Gray50: Color = gray50,
    White: Color = white,
    Red: Color = red,
    Green: Color = green,
    PurpleAlpha: Color = purpleAlpha,
    BlackAlpha: Color = blackAlpha,
    LightPurpleAlpha: Color = lightPurpleAlpha,
    WhiteGrad: Color = whiteGrad,
) = NapzakMarketColors(
    Purple500,
    Purple200,
    Purple100,
    Black,
    Gray500,
    Gray400,
    Gray300,
    Gray200,
    Gray100,
    Gray50,
    White,
    Red,
    Green,
    PurpleAlpha,
    BlackAlpha,
    LightPurpleAlpha,
    WhiteGrad,
    isLight = true
)
