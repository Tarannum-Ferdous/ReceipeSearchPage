package com.example.ReceipeSearchPage.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ReceipeSearchPage.Model.ProductModel;

import java.util.ArrayList;

public class DatabaseHelper2 extends SQLiteOpenHelper {


    public static String DB_NAME = "receipepage1";

    public static int DB_VERSION = 1;

    public DatabaseHelper2(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query="CREATE TABLE history (historyID int primary key,address LONGTEXT)";
        sqLiteDatabase.execSQL(query);
    }
    public long addHistory(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("address", address);

        return db.insert("history", null, values);
    }

    public long deleteHistory(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("history","address='"+address.replace("'","")+"'",null);


    }


    public ArrayList<String> getAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> listItems = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT * from history",new String[] {});

        if (cursor.moveToFirst()) {
            do {
                String image = cursor.getString(1);

                listItems.add(image);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}