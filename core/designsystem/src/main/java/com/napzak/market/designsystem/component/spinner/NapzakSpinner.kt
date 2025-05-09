package com.napzak.market.designsystem.component.spinner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.napzak.market.designsystem.R.drawable.ic_down_chevron
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * 드롭다운 메뉴 형식의 스피너 컴포넌트입니다.옵션 목록과 초기 선택지를 받아 드롭다운 메뉴를 표시합니다.
 *
 * @param options 드롭다운 메뉴에 표시될 옵션 목록입니다.
 * @param initialOption 초기 선택된 옵션입니다.
 * @param onOptionSelect 옵션이 선택될 때 호출되는 콜백입니다. 선택된 옵션을 인자로 전달합니다.
 */

@Composable
fun NapzakSpinner(
    options: ImmutableList<String>,
    initialOption: String,
    onOptionSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: TextFieldColors = spinnerDefaultColor(),
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf(initialOption) }

    val shape = RoundedCornerShape(14.dp)
    val borderModifier = Modifier.border(
        width = Dp.Hairline,
        color = NapzakMarketTheme.colors.gray200,
        shape = shape,
    )

    Column(
        modifier = modifier.then(borderModifier),
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { /*This TextField is just for displaying currently selected option*/ },
            textStyle = NapzakMarketTheme.typography.caption12sb,
            colors = color,
            shape = shape,
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(ic_down_chevron),
                    tint = NapzakMarketTheme.colors.gray200,
                    contentDescription = null,
                )
            },
            modifier = borderModifier
                .fillMaxWidth()
                .noRippleClickable { expanded = !expanded },
        )

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                options.forEach { menu ->
                    DropdownMenuItem(
                        text = menu,
                        isSelected = menu == selectedOption,
                        onClick = {
                            selectedOption = menu
                            onOptionSelect(menu)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun DropdownMenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = NapzakMarketTheme.colors
    val color = if (isSelected) colorScheme.purple500 else colorScheme.gray300
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            style = NapzakMarketTheme.typography.caption12m
                .copy(color = color),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun spinnerDefaultColor(): TextFieldColors = TextFieldDefaults.colors(
    disabledTextColor = NapzakMarketTheme.colors.gray300,
    disabledTrailingIconColor = NapzakMarketTheme.colors.gray300,
    disabledContainerColor = NapzakMarketTheme.colors.white,
    disabledIndicatorColor = NapzakMarketTheme.colors.white,
)

@Preview(showBackground = true)
@Composable
private fun NapzakSpinnerPreview() {
    val mockOptions = listOf("옵션1111", "옵션2222", "옵션3333", "옵션4444")

    NapzakMarketTheme {
        NapzakSpinner(
            options = mockOptions.toImmutableList(),
            initialOption = mockOptions.first(),
            onOptionSelect = { },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
        )
    }
}