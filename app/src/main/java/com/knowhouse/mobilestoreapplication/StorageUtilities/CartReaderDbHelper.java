package com.knowhouse.mobilestoreapplication.StorageUtilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CartReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="CartReader.db";

    private static final String SQL_CREATE_ENTRIES=
            "CREATE TABLE "+ CartReaderContract.CartEntry.TABLE_NAME +
                    " ("+
                    CartReaderContract.CartEntry._ID + " INTEGER PRIMARY KEY,"+
                    CartReaderContract.CartEntry.COLUMN_IMAGE_URL+" TEXT,"+
                    CartReaderContract.CartEntry.COLUMN_FOOD_VALUE_TEXT+" TEXT,"+
                    CartReaderContract.CartEntry.COLUMN_DESCRIPTION_VALUE_TEXT+" TEXT,"+
                    CartReaderContract.CartEntry.COLUMN_QUANTITY_VALUE_TEXT + " TEXT,"+
                    CartReaderContract.CartEntry.COLUMN_OPTION_VALUE_TEXT+ " TEXT,"+
                    CartReaderContract.CartEntry.COLUMN_PRICE_VALUE_TEXT+" TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS "+ CartReaderContract.CartEntry.TABLE_NAME;


    public CartReaderDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
