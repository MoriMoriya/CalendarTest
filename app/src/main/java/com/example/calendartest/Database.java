package com.example.calendartest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 171y065 on 2018/09/27.
 */

public class Database extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "MONEY.db";
    private static final String TABLE_NAME = "Money";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TITLE = "date";
    private static final String COLUMN_NAME_SUBTITLE = "money";

    private static final int DATABASE_VERSION = 4;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_SUBTITLE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void saveData(SQLiteDatabase db, String item, int price){
        ContentValues values = new ContentValues();
        values.put("items", item);
        values.put("price", price);

        db.insert("testdb", null, values);
    }

    /*
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "MoneyDB.db";
    private static final String TABLE_NAME = "moneydb";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME = "day";
    private static final String COLUMN_NAME_SUBTITLE = "money";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE" + TABLE_NAME + "(" + _ID + "INTEGER PRIMARY KEY," +
                    COLUMN_NAME +  "DATE," + COLUMN_NAME_SUBTITLE + "INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS" + TABLE_NAME;

    Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, String item,int money){
        ContentValues values= new ContentValues();
        values.put("Date",item);
        values.put("money",money);

        db.insert("moneydb",null,values);
    }
    */
}
