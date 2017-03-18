package com.map.suba.dontgiveup;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by suba on 1/18/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="listManager";
    private static final String TABLE_LIST="str";

    private static final String KEY_str="listOfString";

    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_lIST_TABLE="CREATE TABLE" + TABLE_LIST +"("+KEY_str+"TEXT" +")";
        db.execSQL(CREATE_lIST_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_LIST);

        onCreate(db);
    }

    //Adding new list
    void addList(CreateList createList){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_str,createList.getTask());

        db.insert(TABLE_LIST,null,values);
        db.close();
    }
}
