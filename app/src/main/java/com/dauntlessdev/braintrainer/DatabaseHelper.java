package com.dauntlessdev.braintrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME ="register.db";
    public static final String TABLE_NAME ="registeruser";
    public static final String COL_2 ="username";
    public static final String COL_3 ="password";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE scoreuser (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, score NUMBER,answered NUMBER)");
        sqLiteDatabase.execSQL("CREATE TABLE settings (ID INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT, music NUMBER,notification NUMBER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addUser(String user, String pwd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, user);
        contentValues.put(COL_3, pwd);

        long res = db.insert("registeruser", null, contentValues);
        db.close();
        return res;
    }




    public String searchPass(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username,password from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a,b;
        b ="not found";
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);

                if(a.equals(user)){
                    b = cursor.getString(1);
                    break;
                }

            }while (cursor.moveToNext());
        }
        return b;
    }

    public boolean checkUsername(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a;
        boolean userExists = false;
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);

                if(a.equals(user)){
                    userExists = true;
                    break;
                }

            }while (cursor.moveToNext());
        }
        return userExists;
    }

    public ArrayList<Integer> getAllScore(String user){
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username,score from scoreuser";
        Cursor cursor = db.rawQuery(query, null);
        String a;
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                if(a.equals(user)){
                    list.add(cursor.getInt(1));
                }

            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<Integer> getAllAnswered(String user){
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username,answered from scoreuser";
        Cursor cursor = db.rawQuery(query, null);
        String a;
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                if(a.equals(user)){
                    list.add(cursor.getInt(1));
                }

            }while (cursor.moveToNext());
        }
        return list;
    }


    public long addScore(String user, int lastscore, int answer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, user);
        contentValues.put("score", lastscore);
        contentValues.put("answered", answer);

        long res = db.insert("scoreuser", null, contentValues);
        db.close();
        return res;
    }


    public long newSettings(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, user);
        contentValues.put("music", 0);
        contentValues.put("notification", 0);

        long res = db.insert("settings", null, contentValues);
        db.close();
        return res;
    }

    public boolean updateMusic(String name, int currMusic) {

        try{

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv= new ContentValues();
            cv.put("music", currMusic);
            db.update("settings", cv, "username='" + name  +"'", null);
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public int getMusic(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username,music from settings";
        Cursor cursor = db.rawQuery(query, null);
        String a;
        int musicx = 0;
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                if(a.equals(user)){
                    musicx = cursor.getInt(1);
                    break;
                }

            }while (cursor.moveToNext());
        }
        return musicx;
    }



    public boolean updateNotif(String name, int currNotif)  {

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv= new ContentValues();
            cv.put("notification", currNotif);
            db.update("settings", cv, "username='" + name  +"'" , null);
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public int getNotif(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username,notification from settings";
        Cursor cursor = db.rawQuery(query, null);
        String a;
        int musicx = 0;
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                if(a.equals(user)){
                    musicx = cursor.getInt(1);
                    break;
                }

            }while (cursor.moveToNext());
        }
        return musicx;
    }
}
