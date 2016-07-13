package com.example.yegor.cafeapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.yegor.cafeapp.models.RealmCategoryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLiteClass {

    private static final String DATABASE_NAME = "CATEGORY_DB";
    private static final int DATABASE_VERSION = 1;
    private static final String CATEGORY_TABLE = "CATEGORY_TABLE";


    private Context context;
    private DBHelp dbhelp;
    private SQLiteDatabase thisDataBase;

    public MySQLiteClass(Context context) {
        this.context = context;
    }

    public MySQLiteClass open(boolean writable) throws SQLiteException {

        dbhelp = new DBHelp(context);

        if (writable)
            thisDataBase = dbhelp.getWritableDatabase();
        else
            thisDataBase = dbhelp.getReadableDatabase();

        return this;
    }

    public void close() {
        dbhelp.close();
    }

    public boolean isCatTableExists() {

        if (dbhelp == null)
            dbhelp = new DBHelp(context);

        //open(true);
        boolean tableExists = dbhelp.isTableExists(thisDataBase, CATEGORY_TABLE);
        //close();

        return tableExists;
    }

    private void addCategory(RealmCategoryModel category) {

        ContentValues values = new ContentValues();

        values.put(RealmCategoryModel.ID, category.getId());
        values.put(RealmCategoryModel.NAME, category.getCategory());
        values.put(RealmCategoryModel.IMAGE, category.getImage());

        thisDataBase.insert(CATEGORY_TABLE, null, values);

    }

    public void addCategories(List<RealmCategoryModel> categories) {

        open(true);

        String sql = "INSERT INTO " + CATEGORY_TABLE + " VALUES (?,?,?,?);";
        SQLiteStatement statement = thisDataBase.compileStatement(sql);

        thisDataBase.beginTransaction();

        int i = 1;

        for (RealmCategoryModel model : categories) {

            statement.clearBindings();
            statement.bindLong(1, i);
            statement.bindLong(2, model.getId());
            statement.bindString(3, model.getCategory());
            statement.bindLong(4, model.getImage());

            statement.execute();

            i++;
        }

        thisDataBase.setTransactionSuccessful();
        thisDataBase.endTransaction();

        close();
    }

    public List<RealmCategoryModel> getAllCategories() {

        List<RealmCategoryModel> categories = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;

        open(false);

        Cursor cursor = thisDataBase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                RealmCategoryModel contact = new RealmCategoryModel.Builder()
                        .setId(Integer.parseInt(cursor.getString(1)))
                        .setCategory(cursor.getString(2))
                        .setImage(Integer.parseInt(cursor.getString(3)))
                        .create();

                categories.add(contact);

            } while (cursor.moveToNext());
        }

        cursor.close();
        close();

        return categories;
    }

    public Map<Integer, Integer> getId2ImageResMap() {

        Map<Integer, Integer> map = new HashMap<>();

        open(false);

        Cursor cursor = thisDataBase.query(
                CATEGORY_TABLE,
                new String[]{RealmCategoryModel.ID, RealmCategoryModel.IMAGE},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                map.put(cursor.getInt(0), cursor.getInt(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();

        return map;
    }

    private class DBHelp extends SQLiteOpenHelper {

        private final String CREATE_CURRENCY_TABLE =
                "CREATE TABLE " + CATEGORY_TABLE + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RealmCategoryModel.ID + " INTEGER, " +
                        RealmCategoryModel.NAME + " TEXT NOT NULL, " +
                        RealmCategoryModel.IMAGE + " INTEGER);";

        public DBHelp(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CURRENCY_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        boolean isTableExists(SQLiteDatabase db, String tableName) {

            if (tableName == null || db == null || !db.isOpen()) {//// TODO: 12.07.16 open db
                return false;
            }
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                    new String[]{"table", tableName});

            if (!cursor.moveToFirst()) {
                return false;
            }
            int count = cursor.getInt(0);
            cursor.close();

            return count > 0;
        }

    }

}
