package com.yodamob.sample.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tychooo on 2017/6/6.
 */

public class YodamobSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_AD_CONFIGURATIONS = "adConfigurations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AD_UNIT_ID = "adUnitId";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_USER_GENERATED = "userGenerated";
    public static final String COLUMN_AD_TYPE = "adType";

    private static final String DATABASE_NAME = "saveConfigurations.db";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE = "create table " + TABLE_AD_CONFIGURATIONS
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AD_UNIT_ID + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_USER_GENERATED + " integer not null, "
            + COLUMN_AD_TYPE + " text not null"
            + ");";

    public YodamobSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(YodamobSQLiteHelper.class.getName(), "调整数据版本, 旧版本为" + oldVersion + "新版本为" + newVersion);
        recreatedDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(YodamobSQLiteHelper.class.getName(), "调整数据版本, 旧版本为" + oldVersion + "新版本为" + newVersion);
        recreatedDb(db);
    }

    private void recreatedDb(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS" + TABLE_AD_CONFIGURATIONS);
        onCreate(database);
    }
}
