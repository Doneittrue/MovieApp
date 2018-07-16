package com.example.khale.moviesnap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by khale on 11/16/2016.
 */

public class SnapAppDBHelper extends SQLiteOpenHelper {
    //we defingin the database name and version
    static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    //the constructor takes the context as an argument
    public SnapAppDBHelper(Context context) {
        //then we pass the database name ,database version and context to its super class
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //inside onCreate method we write query to create the table
        final String MOVIE_TABLE_CREATE = "CREATE TABLE " + SnapAppContact.MovieEntry.TABLE_NAME + " (" +
                SnapAppContact.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SnapAppContact.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                SnapAppContact.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                SnapAppContact.MovieEntry.COLUMN_MAIN_IMAGE + " TEXT, " +
                SnapAppContact.MovieEntry.COLUMN_DESCRIPTION + " TEXT, " +
                SnapAppContact.MovieEntry.COLUMN_RATING + " TEXT, " +
                SnapAppContact.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                SnapAppContact.MovieEntry.COLUMN_RELEASED_DATE + " TEXT);";

        db.execSQL(MOVIE_TABLE_CREATE);
        //log for testing
        Log.i("qdsdsdsdaaaa","databse was created");

    }

    //when the database updated we use this method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SnapAppContact.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
