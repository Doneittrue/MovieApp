package com.example.khale.moviesnap;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by khale on 11/16/2016.
 */

public class SnapUtility {
    public static int isChecked(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                SnapAppContact.MovieEntry.CONTENT_URI,
                null,   // projection
                SnapAppContact.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[]{Integer.toString(id)},   // selectionArgs
                null    // sort order
        );

        //to check if there an row or not
        int rows_num = cursor.getCount();
        //Log for testing
        Log.i("rotowqo",rows_num+"");
        //ew close the curser here
        cursor.close();
        return rows_num;
    }
}
