package com.example.android.bookapp.data;

import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {
    }

    public static abstract class BookEntry implements BaseColumns {

        public final static String TABLE_NAME = "books";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "name";

        public final static String COLUMN_PRODUCT_PRICE = "price";

        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";


        public static final int QUANTITY0 = 0;
        public static final int QUANTITY1 = 1;

        public final static String COLUMN_SUPPLIER_NAME = "supplierName";
        public final static String COLUMN_SUPPLIER_PHONE = "supplierPhone";
    }
}

