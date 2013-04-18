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


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class ReminderContentProvider extends ContentProvider{

	public static final String AUTHORITY = "com.rbarnes.other.ReminderContentProvider";
	private static final String BASE_PATH = "reminders";
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	        + "/reminderDB";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	        + "/reminderDB";
	
	
	
	private SQLiteDatabase db;
	 private static final UriMatcher sURIMatcher = makeUriMatcher();
	//URiMatcher to match client URis
	 public static final int REMINDERS = 1;
	 public static final int REMINDER = 2;
	 
	 private static UriMatcher makeUriMatcher() {

		    UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		    matcher.addURI(AUTHORITY,  BASE_PATH , REMINDERS );
			  matcher.addURI(AUTHORITY,  BASE_PATH + "/#", REMINDER);
		  
		    return matcher;
		}
	
	
	 
	  
	
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int retVal = db.delete(ReminderDB.TABLE_NAME, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		return retVal;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case REMINDERS:
            return CONTENT_TYPE;
        case REMINDER:
            return CONTENT_ITEM_TYPE;
        default:
            return null;
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues inValues) {
		ContentValues values = new ContentValues(inValues);
		long rowId = db.insertOrThrow(ReminderDB.TABLE_NAME, null, values);
		if(rowId > 0){
			Uri url = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(url, null);
		
		return uri;
		}else{
		throw new SQLException("Failed to insert row into " + uri);
		}

	}

	@Override
	public boolean onCreate() {
		db = new ReminderDB(getContext()).getWritableDatabase();

		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		Log.i("CURSOR_URI", uri.toString());
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ReminderDB.TABLE_NAME);
        
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case REMINDER:
            queryBuilder.appendWhere(ReminderDB.ID + "="
                    + uri.getLastPathSegment());
            break;
        case REMINDERS:
            // no filter
            break;
        default:
            
        }
        
			Cursor c =  queryBuilder.query(db,projection, selection, selectionArgs, null, null, sort);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;

		

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int retVal = db.update(ReminderDB.TABLE_NAME, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		return retVal;
	}

	
}
