package com.ananas.shrink.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    
    public Database(Context context){
        super(context, "Shrink.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String command = "CREATE TABLE links (";
        command += "id INTEGER,"; // 0
        command += "long_link TEXT NOT NULL,"; // 1
        command += "short_link TEXT NOT NULL,"; // 2
        command += "date TEXT,"; // 3
        command += "PRIMARY KEY(\"id\" AUTOINCREMENT)";
        command += ");";
        sqLiteDatabase.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS links;");
    }

    public Boolean addNewLink(String _short_link, String _long_link, String _date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("long_link", _long_link);
        cv.put("short_link", _short_link);
        cv.put("date", _date);
        long ok = db.insert("links", null, cv);
        return (ok != -1);
    }

    public Cursor loadLinks(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM links;", null);
    }

    public Cursor loadLink(int Id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM links WHERE id = '" + String.valueOf(Id) + "';", null);
    }

    public void deleteLink(int Id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM links WHERE id = '" + String.valueOf(Id) + "';");
    }
    
}
