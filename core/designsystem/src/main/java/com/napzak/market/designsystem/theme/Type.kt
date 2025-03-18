package com.napzak.market.designsystem.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

@Stable
class NapzakMarketTypography(
    title22b: TextStyle,
    title20b: TextStyle,
    title20sb: TextStyle,
    title18b: TextStyle,
    title18sb: TextStyle,
    title18m: TextStyle,
    body16b: TextStyle,
    body16sb: TextStyle,
    body16m: TextStyle,
    body16r: TextStyle,
    body14b: TextStyle,
    body14sb: TextStyle,
    body14r: TextStyle,
    caption12sb: TextStyle,
    caption12m: TextStyle,
    caption12r: TextStyle,
    caption10sb: TextStyle,
    caption10r: TextStyle,
) {
    var title22b by mutableStateOf(title22b)
        private set
    var title20b by mutableStateOf(title20b)
        private set
    var title20sb by mutableStateOf(title20sb)
        private set
    var title18b by mutableStateOf(title18b)
        private set
    var title18sb by mutableStateOf(title18sb)
        private set
    var title18m by mutableStateOf(title18m)
        private set
    var body16b by mutableStateOf(body16b)
        private set
    var body16sb by mutableStateOf(body16sb)
        private set
    var body16m by mutableStateOf(body16m)
        private set
    var body16r by mutableStateOf(body16r)
        private set
    var body14b by mutableStateOf(body14b)
        private set
    var body14sb by mutableStateOf(body14sb)
        private set
    var body14r by mutableStateOf(body14r)
        private set
    var caption12sb by mutableStateOf(caption12sb)
        private set
    var caption12m by mutableStateOf(caption12m)
        private set
    var caption12r by mutableStateOf(caption12r)
        private set
    var caption10sb by mutableStateOf(caption10sb)
        private set
    var caption10r by mutableStateOf(caption10r)
        private set

    fun copy(
        title22b: TextStyle = this.title22b,
        title20b: TextStyle = this.title20b,
        title20sb: TextStyle = this.title20sb,
        title18b: TextStyle = this.title18b,
        title18sb: TextStyle = this.title18sb,
        title18m: TextStyle = this.title18m,
        body16b: TextStyle = this.body16b,
        body16sb: TextStyle = this.body16sb,
        body16m: TextStyle = this.body16m,
        body16r: TextStyle = this.body16r,
        body14b: TextStyle = this.body14b,
        body14sb: TextStyle = this.body14sb,
        body14r: TextStyle = this.body14r,
        caption12sb: TextStyle = this.caption12sb,
        caption12m: TextStyle = this.caption12m,
        caption12r: TextStyle = this.caption12r,
        caption10sb: TextStyle = this.caption10sb,
        caption10r: TextStyle = this.caption10r,
    ): NapzakMarketTypography = NapzakMarketTypography(
        title22b,
        title20b,
        title20sb,
        title18b,
        title18sb,
        title18m,
        body16b,
        body16sb,
        body16m,
        body16r,
        body14b,
        body14sb,
        body14r,
        caption12sb,
        caption12m,
        caption12r,
        caption10sb,
        caption10r,
    )

    fun update(other: NapzakMarketTypography) {
        title22b = other.title22b
        title20b = other.title20b
        title20sb = other.title20sb
        title18b = other.title18b
        title18sb = other.title18sb
        title18m = other.title18m
        body16b = other.body16b
        body16sb = other.body16sb
        body16m = other.body16m
        body16r = other.body16r
        body14b = other.body14b
        body14sb = other.body14sb
        body14r = other.body14r
        caption12sb = other.caption12sb
        caption12m = other.caption12m
        caption12r = other.caption12r
        caption10sb = other.caption10sb
        caption10r = other.caption10r
    }
}

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

@Composable
fun NapzakMarketTypography(): NapzakMarketTypography {
    return NapzakMarketTypography(
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
}

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