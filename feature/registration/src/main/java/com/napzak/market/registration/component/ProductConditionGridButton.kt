package com.napzak.market.registration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.napzak.market.common.type.ProductConditionType
import com.napzak.market.designsystem.R.drawable.ic_check_purple
import com.napzak.market.designsystem.theme.NapzakMarketTheme
import com.napzak.market.util.android.noRippleClickable

private const val ITEM_CHUNK = 2

/**
 * Product condition grid button
 *
 * @param selectedCondition: 선택된 상태
 * @param onConditionSelected: 상태 선택
 */
@Composable
internal fun ProductConditionGridButton(
    selectedCondition: ProductConditionType?,
    onConditionSelected: (ProductConditionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ProductConditionType.entries.chunked(ITEM_CHUNK).forEach { rowItems ->
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                rowItems.forEach { item ->
                    ProductConditionItem(
                        condition = item.label,
                        isSelected = selectedCondition == item,
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable { onConditionSelected(item) },
                    )
                }
            }
        }
    }
}

/**
 * Product condition item
 *
 * @param condition: 상품의 상태
 * @param isSelected: 선택 여부
 */
@Composable
private fun ProductConditionItem(
    condition: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val contentColor =
        if (isSelected) NapzakMarketTheme.colors.purple500 else NapzakMarketTheme.colors.gray200
    val icon = if (isSelected) ic_check_purple else null

    Row(
        modifier = modifier
            .background(NapzakMarketTheme.colors.white, RoundedCornerShape(8.dp))
            .border(1.dp, contentColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 21.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = condition,
            style = NapzakMarketTheme.typography.body14sb.copy(
                color = contentColor,
            ),
        )
        icon?.let { icon ->
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = contentColor,
            )
        }
    }
}

@Preview
@Composable
private fun ProductConditionGridButtonPreview() {
    NapzakMarketTheme {
        var selectedItem by remember { mutableStateOf<ProductConditionType?>(null) }
        ProductConditionGridButton(
            onConditionSelected = { selectedItem = it },
            modifier = Modifier.fillMaxWidth(),
            selectedCondition = selectedItem
        )
    }
}
