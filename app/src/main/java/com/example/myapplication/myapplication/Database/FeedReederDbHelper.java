package com.example.myapplication.myapplication.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.myapplication.Models.Training;

import java.util.ArrayList;

public class FeedReederDbHelper extends SQLiteOpenHelper {
    private FeedReederDbHelper(Context context){ super(context,DATABASE_NAME,null,DATABASE_VERSION); }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES); }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);onCreate(sqLiteDatabase); }
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydb";
    public static final String TABLE_NAME="training";
    public static final String ID="id";
    public static final String DATE="date";
    public static final String DISTANCE="distance";
    public static final String CALORIES="calories";
    public static final String DURATION="duration";
    public static final String STEPS="steps";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +TABLE_NAME + " (" +
                    ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DATE + " TEXT," +
                    DISTANCE + " TEXT,"+
                    CALORIES+ " TEXT,"+
                    DURATION+ " TEXT,"+
                    STEPS+ " TEXT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS mydb";
    public static FeedReederDbHelper instance=null;

    public static FeedReederDbHelper getInstance(Context c){
        if(instance==null){
            instance=new FeedReederDbHelper(c.getApplicationContext());
        }
        return instance;
    }
    public void insert(String sql){
        this.getWritableDatabase().execSQL(sql);
    }
    public ArrayList<Training> getAll(){
        String sql="SELECT * FROM training";
        Cursor c=this.getReadableDatabase().rawQuery(sql,null);
        ArrayList<Training>lista=new ArrayList<>();
        int i=0;
        if(c.moveToFirst()){
            do{
                String datum=c.getString(1);
                String distance=c.getString(2);
                String calories=c.getString(3);
                String duration=c.getString(4);
                String steps=c.getString(5);
                lista.add(new Training(datum,steps,distance,duration,calories));
            }while(c.moveToNext());
        }
        return lista;
    }



}
