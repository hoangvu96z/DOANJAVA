package com.hvstudy.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 12/25/2016.
 */

public class SQLite extends SQLiteOpenHelper {

    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE TRUYENOFFLINE(username TEXT PRIMARY KEY,idtruyen INTEGER, idchuong INTEGER "
                        +"tuadetruyen TEXT, tuadechuong TEXT, noidung TEXT )";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS TRUYENOFFLINE");

        // Recreate
        onCreate(db);
    }

    public void addNote (NoteTruyenOffline note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",note.getUsername());
        values.put("idtruyen",note.getIdtruyen());
        values.put("idchuong",note.getIdchuong());
        values.put("tuadetruyen",note.getTuadetruyen());
        values.put("tuadechuong",note.getTuadechuong());
        values.put("noidung",note.getNoidung());
        db.insert("TRUYENOFFLINE",null,values);
        db.close();
    }

    public List<NoteTruyenOffline> getNoteByUser(String user) {
        List<NoteTruyenOffline> list = new ArrayList<NoteTruyenOffline>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM TRUYENOFFLINE WHERE TRUYENOFFLINE.username='"+user+"' GROUP BY idtruyen";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                NoteTruyenOffline note = new NoteTruyenOffline();
                note.setUsername(cursor.getString(0));
                note.setIdchuong(Integer.parseInt(cursor.getString(2)));
                note.setIdtruyen(Integer.parseInt(cursor.getString(1)));
                note.setTuadetruyen(cursor.getString(3));
                note.setTuadechuong(cursor.getString(4));
                list.add(note);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
