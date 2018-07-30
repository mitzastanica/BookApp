package com.example.android.bookapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.bookapp.data.BookContract.BookEntry.TABLE_NAME;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "books.db";

    private static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + com.example.android.bookapp.data.BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + com.example.android.bookapp.data.BookContract.BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + com.example.android.bookapp.data.BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + com.example.android.bookapp.data.BookContract.BookEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL, "
                + com.example.android.bookapp.data.BookContract.BookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + com.example.android.bookapp.data.BookContract.BookEntry.COLUMN_SUPPLIER_PHONE + " INTEGER NOT NULL);";

        database.execSQL(SQL_CREATE_PRODUCT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the Database
        db.execSQL("DROP TABLE IF EXISTS " + BookContract.BookEntry.TABLE_NAME);
        // Create a new one
        onCreate(db);
    }


}
