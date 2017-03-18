package com.map.suba.dontgiveup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by suba on 1/18/2017.
 */

public class DBAdapter {


    private static final String TAG= "DBAdapter"; //used for loging database version changes
    //Field name
    public static final String KEY_ROWID="_id";
    public static final String KEY_TASK="task";

    public static final String KEY_DATE="date";

    public static String getKeyRowid() {
        return KEY_ROWID;
    }

    public static String getKeyTask() {
        return KEY_TASK;
    }

    public static String getKeyDate() {
        return KEY_DATE;
    }

    public static final String[] ALL_KEYS=new String[]{KEY_ROWID,KEY_TASK,KEY_DATE};


    //Database info
    public static final String DATABASE_NAME="dbToDo";
    public static final String DATABASE_TABLE="mainToDo";
    public static final int DATABASE_VERSION=1;

    private static final String DATABASE_CREATE_SQL="CREATE TABLE "+DATABASE_TABLE+"("+KEY_ROWID
            +" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_TASK+" TEXT NOT NULL, "+KEY_DATE+" TEXT NOT NULL"+");";

    private static final String SORT_TABLE="SELECT "+"* "+"FROM "+DATABASE_TABLE+" ORDER BY date("+KEY_DATE+") DESC";


    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context ctx){
        this.context=ctx;
        myDBHelper=new DatabaseHelper(context);

    }

    //Open database connection

    public DBAdapter open(){
        db=myDBHelper.getWritableDatabase();
        return this;
    }
    //Close database connection
    public void close(){
        myDBHelper.close();
    }

    //Add a new set of values to be inserted into the database
    public long insertRow(String task, String date){
        ContentValues initialValues =new ContentValues();
        initialValues.put(KEY_TASK,task);
        initialValues.put(KEY_DATE,date);
        //Insert the data into database
        return db.insert(DATABASE_TABLE,null,initialValues);
    }

    public void sort(){
        db.rawQuery(SORT_TABLE,null);
    }

    //Delete a row from the database, by rowId
    public boolean deleteRow(long rowId){
        String where =KEY_ROWID + "="+rowId;
        return db.delete(DATABASE_TABLE,where,null)!=0;
    }

    public void deleteAll(){
        Cursor c=getAllRows();
        long rowId=c.getColumnIndexOrThrow(KEY_ROWID);
        if(c.moveToFirst()){
            do{
                deleteRow(c.getLong((int)rowId));
            }while (c.moveToNext());
        }
        c.close();
    }

    //return all the data in the database
    public Cursor getAllRows(){
        String where =null;
        Cursor c=db.query(true,DATABASE_TABLE,ALL_KEYS,where,null,null,null,KEY_DATE+" ASC",null);
        if(c!=null){
            c.moveToFirst();
        }
        return  c;
    }
    //Get specific row by rowId

    public Cursor getRow(long rowId){
        String where=KEY_ROWID+"="+rowId;
        Cursor c=db.query(true,DATABASE_TABLE,ALL_KEYS,where,null,null,null,null,null);
        if(c!=null){
            c.moveToFirst();
        }
        return  c;
    }


    //Change an existing row to be equal to new data
    public boolean updateRow(long rowId, String date){
        String where=KEY_ROWID+"="+rowId;
        ContentValues newValues=new ContentValues();
        newValues.put(KEY_DATE, date);
        return db.update(DATABASE_TABLE,newValues,where,null)!=0;
    }

    public void remove (int position){

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase _db){
            _db.execSQL(DATABASE_CREATE_SQL);
        }
        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading application's database from version" +oldVersion
                    +"to"+newVersion+", which will destory all old data!");
            //Destory old database
            _db.execSQL("DROP TABLE IF EXISTS"+DATABASE_TABLE);

            //Recreate new database
            onCreate(_db);
        }

    }

}
