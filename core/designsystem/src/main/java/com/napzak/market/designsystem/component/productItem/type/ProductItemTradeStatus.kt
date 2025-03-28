package com.napzak.market.designsystem.component.productItem.type

import androidx.annotation.StringRes
import com.napzak.market.designsystem.R.string.production_item_complete
import com.napzak.market.designsystem.R.string.production_item_on_sale
import com.napzak.market.designsystem.R.string.production_item_reserved

enum class ProductItemTradeStatus(
    @StringRes val text: Int,
) {
    OnSale(production_item_on_sale),
    Sold(production_item_complete),
    Reserved(production_item_reserved);
}