package com.example.kseniya57.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "products.db";
    final static String TABLE_NAME = "products";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(64) NOT NULL, price INTEGER NOT NULL, type VARCHAR(32) NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
