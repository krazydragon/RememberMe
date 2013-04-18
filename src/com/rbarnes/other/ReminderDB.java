/*
 * project	RememberMe
 * 
 * package	com.rbarnes.other
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 18, 2013
 */
package com.rbarnes.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SdCardPath")
public class ReminderDB extends SQLiteOpenHelper{

	
	private static final String DEBUG_TAG = "LocationDatabase";
	private static String DB_PATH = "/data/data/com.mdf3w2.rbarnes.rememberme/databases/";
	public static final String DB_NAME = "locationDB";
	private static final int DB_VERSION = 1;
	
	public static final String TABLE_NAME = "locations";
	public static final String ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_IMG = "images";
	public static final String COL_COORDS = "cords";
	private static final String CREATE_TABLE = "create table " + TABLE_NAME	+ " (" + ID + " integer primary key autoincrement, " + COL_TITLE + " text, " + COL_IMG + " text, " + COL_COORDS + " text);";

	private static final String DB_SCHEMA = CREATE_TABLE;		
			
			
	public ReminderDB(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(DB_SCHEMA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w(DEBUG_TAG, "Upgrading database. Existing contents will be lost. ["
	            + oldVersion + "]->[" + newVersion + "]");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	    
	    
	}

	public static int checkDataBase(){
		 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		
 
    	}
    	Cursor dbCursor = checkDB.rawQuery("select * from locations", null);
    	if(checkDB != null){
    		//check database count to see if empty 
    		
    	    Log.i("DATABASE COUNT", ""+ dbCursor.getCount());
    		checkDB.close();
 
    	}
 
    	return dbCursor.getCount();
    }

	
}
