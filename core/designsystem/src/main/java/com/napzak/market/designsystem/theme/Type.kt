package com.napzak.market.designsystem.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.napzak.market.designsystem.R

val PretendardBold = FontFamily(Font(R.font.pretendard_bold, FontWeight.Bold))
val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_medium, FontWeight.Normal))

@Immutable
class NapzakMarketTypography(
    val title22b: TextStyle,
    val title20b: TextStyle,
    val title20sb: TextStyle,
    val title18b: TextStyle,
    val title18sb: TextStyle,
    val title18m: TextStyle,
    val body16b: TextStyle,
    val body16sb: TextStyle,
    val body16m: TextStyle,
    val body16r: TextStyle,
    val body14b: TextStyle,
    val body14sb: TextStyle,
    val body14r: TextStyle,
    val caption12sb: TextStyle,
    val caption12m: TextStyle,
    val caption12r: TextStyle,
    val caption10sb: TextStyle,
    val caption10r: TextStyle,
)

private fun NapzakMarketTextStyle(
    fontFamily: FontFamily,
    fontSize: TextUnit,
    lineHeight: TextUnit = 1.28.em,
    letterSpacing: TextUnit = 0.02.em,
): TextStyle = TextStyle(
    fontFamily = fontFamily,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
)

fun NapzakMarketTypography() = NapzakMarketTypography(
    title22b = NapzakMarketTextStyle(
        fontFamily = PretendardBold,
        fontSize = 22.sp
    ),
    title20b = NapzakMarketTextStyle(
        fontFamily = PretendardBold,
        fontSize = 20.sp
    ),
    title20sb = NapzakMarketTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 20.sp
    ),
    title18b = NapzakMarketTextStyle(
        fontFamily = PretendardBold,
        fontSize = 18.sp
    ),
    title18sb = NapzakMarketTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 18.sp
    ),
    title18m = NapzakMarketTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 18.sp
    ),
    body16b = NapzakMarketTextStyle(
        fontFamily = PretendardBold,
        fontSize = 16.sp
    ),
    body16sb = NapzakMarketTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 16.sp
    ),
    body16m = NapzakMarketTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 16.sp
    ),
    body16r = NapzakMarketTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 16.sp
    ),
    body14b = NapzakMarketTextStyle(
        fontFamily = PretendardBold,
        fontSize = 14.sp
    ),
    body14sb = NapzakMarketTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 14.sp
    ),
    body14r = NapzakMarketTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 14.sp
    ),
    caption12sb = NapzakMarketTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 12.sp
    ),
    caption12m = NapzakMarketTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 12.sp
    ),
    caption12r = NapzakMarketTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 12.sp
    ),
    caption10sb = NapzakMarketTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 10.sp
    ),
    caption10r = NapzakMarketTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 10.sp
    ),
)

@Preview(showBackground = true)
@Composable
fun NapzakMarketTypographyPreview() {
    NapzakMarketTheme {
        Column {
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.title22b
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.title20b
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.title20sb
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.title18b
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.title18sb
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.title18m
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.body16b
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.body16sb
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.body16m
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.body16r
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.body14b
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.body14sb
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.body14r
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.caption12sb
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.caption12m
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.caption12r
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.caption10sb
            )
            Text(
                "NapzakMarketTheme",
                style = NapzakMarketTheme.typography.caption10r
            )
        }
    }
}
