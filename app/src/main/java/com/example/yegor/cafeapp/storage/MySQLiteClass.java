package com.example.yegor.cafeapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yegor.cafeapp.models.CategoryModel;

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
        return dbhelp.isTableExists(thisDataBase, CATEGORY_TABLE);
    }

    private void addCategory(CategoryModel category) {

        ContentValues values = new ContentValues();

        values.put(CategoryModel.ID, category.getId());
        values.put(CategoryModel.NAME, category.getCategory());
        values.put(CategoryModel.IMAGE, category.getImage());

        thisDataBase.insert(CATEGORY_TABLE, null, values);

    }

    public void addCategories(List<CategoryModel> categories) {

        open(true);

        for (CategoryModel category : categories)
            addCategory(category);

        close();

    }

    public List<CategoryModel> getAllCategories() {

        List<CategoryModel> categories = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;

        open(false);

        Cursor cursor = thisDataBase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                CategoryModel contact = new CategoryModel.Builder()
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
                new String[]{CategoryModel.ID, CategoryModel.IMAGE},
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
                        CategoryModel.ID + " INTEGER, " +
                        CategoryModel.NAME + " TEXT NOT NULL, " +
                        CategoryModel.IMAGE + " INTEGER);";

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
            if (tableName == null || db == null || !db.isOpen()) {
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
