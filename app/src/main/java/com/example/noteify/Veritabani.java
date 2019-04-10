package com.example.noteify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Veritabani extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="veritabani";
    private static final int DATABASE_VERSION=1;
    private static final String KISILER_TABLE="kisiler";

    public static final String ROW_ID="id";
    public static final String ROW_NOTE="nots";
    public static final String ROW_TIME="telefon";

    public Veritabani(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + KISILER_TABLE + "(" + ROW_ID + " INTEGER PRIMARY KEY, " + ROW_NOTE + " TEXT NOT NULL, "+ ROW_TIME + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KISILER_TABLE);
        onCreate(db);

    }

    public void VeriEkle(String ad, String time){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ROW_NOTE,ad);
        cv.put(ROW_TIME,time);

        db.insert(KISILER_TABLE,null,cv);
        db.close();
    }

    public List<String> VeriListele(){
        List<String> veriler = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar = {ROW_ID, ROW_NOTE, ROW_TIME};
        Cursor cursor = db.query(KISILER_TABLE,sutunlar,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            veriler.add(cursor.getInt(0) + " - " + cursor.getString(1) + " - " + cursor.getString(2));
        }

        return veriler;

    }

    public void VeriSil(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(KISILER_TABLE,ROW_ID + "=" + id, null);
        db.close();
    }
}
















