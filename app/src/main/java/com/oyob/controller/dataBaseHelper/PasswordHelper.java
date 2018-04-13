package com.oyob.controller.dataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ramasamy on 9/7/2017.
 */

public class PasswordHelper extends SQLiteOpenHelper {
    public static final String TABLENAME = "passwordTable";
    public static final String DBNAME = "password.db";
    public static final String COL1 = "ID";
    public static final String COL2 = "PASSWORD";

    public PasswordHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLENAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT,PASSWORD TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE TABLENAME IF EXISTS"+TABLENAME);
    }

    public boolean insert(String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, password);
        Long result = sqLiteDatabase.insert(TABLENAME, null, contentValues);
        sqLiteDatabase.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isExist(String password) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cur = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLENAME + " WHERE PASSWORD = '" + password + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        sqLiteDatabase.close();
        return exist;
    }

}
