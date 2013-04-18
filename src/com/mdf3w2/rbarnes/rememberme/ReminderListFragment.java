/*
 * project	RememberMe
 * 
 * package	com.mdf3w2.rbarnes.rememberme
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Apr 18, 2013
 */
package com.mdf3w2.rbarnes.rememberme;

import java.util.HashMap;

import com.rbarnes.other.ReminderContentProvider;
import com.rbarnes.other.ReminderDB;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ReminderListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private OnReminderSelectedListener reminderSelectedListener;
    private static final int REMINDER_LIST_LOADER = 2;

    private SimpleCursorAdapter adapter;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String projection[] = { ReminderDB.COL_TITLE, ReminderDB.COL_IMG, ReminderDB.COL_COORDS};
        Cursor reminderCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(ReminderContentProvider.CONTENT_URI,
                        String.valueOf(id)), projection, null, null, null);
        if (reminderCursor.moveToFirst()) {
        	HashMap<String, String> currentReminder = new HashMap<String, String>();
        	
        	currentReminder.put("Title",  reminderCursor.getString(0));
        	currentReminder.put("Image", reminderCursor.getString(1));
        	currentReminder.put("Coords", reminderCursor.getString(2));
			
        	
        	
        	
        	Log.i("ITEM",reminderCursor.getString(0) );
        	
        	reminderSelectedListener.onReminderSelected(currentReminder);
        }
        reminderCursor.close();
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] uiBindFrom = { ReminderDB.COL_TITLE };
        int[] uiBindTo = { R.id.title };

        getLoaderManager().initLoader(REMINDER_LIST_LOADER, null, this);

        adapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.list_item,
                null, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        setListAdapter(adapter);
        setHasOptionsMenu(true);
        
        
    }

    public interface OnReminderSelectedListener {
        public void onReminderSelected(HashMap<String, String> currentLocation);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            reminderSelectedListener = (OnReminderSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLocationSelectedListener");
        }
    }

    

    // LoaderManager.LoaderCallbacks<Cursor> methods

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { ReminderDB.ID, ReminderDB.COL_TITLE };

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
        		ReminderContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
