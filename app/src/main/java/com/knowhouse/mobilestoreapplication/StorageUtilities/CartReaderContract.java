package com.knowhouse.mobilestoreapplication.StorageUtilities;

import android.provider.BaseColumns;

public final class CartReaderContract {
    private CartReaderContract(){}

    public static class CartEntry implements BaseColumns{
        public static final String TABLE_NAME="cart";
        public static final String COLUMN_IMAGE_URL ="image_url";
        public static final String COLUMN_FOOD_VALUE_TEXT="food_text";
        public static final String COLUMN_DESCRIPTION_VALUE_TEXT="description_text";
        public static final String COLUMN_QUANTITY_VALUE_TEXT="quantity_text";
        public static final String COLUMN_OPTION_VALUE_TEXT="option_text";
        public static final String COLUMN_PRICE_VALUE_TEXT="price_text";
    }
}
