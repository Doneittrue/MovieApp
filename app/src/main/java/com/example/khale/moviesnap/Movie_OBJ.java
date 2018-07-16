package com.example.khale.moviesnap;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by khale on 10/27/2016.
 */

public class Movie_OBJ implements Parcelable {

    private String M_Title;
    private String M_RatingLevel;
    private String M_Overview;
    private String M_ImageURL ;
    private String M_ReleasedDate;
    private String M_OriginalLang;
    private int M_Movie_id;

    public Movie_OBJ(String Title, String RatingLevel, String Overview, String ImageURL, String ReleasedDate,String OriginalLang,int Movie_id)
    {
        this.M_Movie_id=Movie_id;
        this.M_Title = Title;
        this.M_RatingLevel = RatingLevel;
        this.M_Overview = Overview;
        this.M_ImageURL = ImageURL;
        this.M_ReleasedDate = ReleasedDate;
        this.M_OriginalLang=OriginalLang;
    }



    //anoyher constuctor to get data from the cursor object that passed to movie and
    //initailizing all of its member varibles with it .
    public Movie_OBJ(Cursor cursor) {
        this.M_Movie_id = cursor.getInt(Movie_MainFragment.COL_MOVIE_ID);
        this.M_Title = cursor.getString(Movie_MainFragment.COL_TITLE);
        this.M_ImageURL = cursor.getString(Movie_MainFragment.COL_MAIN_IMAGE);
        this.M_Overview = cursor.getString(Movie_MainFragment.COL_DESCRIPTION);
        this.M_RatingLevel = cursor.getString(Movie_MainFragment.COL_RATING);
        this.M_OriginalLang = cursor.getString(Movie_MainFragment.COL_ORGINAIL_LANGUAGE);
        this.M_ReleasedDate = cursor.getString(Movie_MainFragment.COL_RELEASED_DATE);
    }

    private Movie_OBJ(Parcel in){
        M_Title = in.readString();
        M_RatingLevel = in.readString();
        M_Overview = in.readString();
        M_ImageURL = in.readString();
        M_ReleasedDate = in.readString();
        M_OriginalLang = in.readString();
        M_Movie_id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setTitle(String Title)
    {
        this.M_Title=Title;
    }
    public String getTitle()
    {
        return M_Title;
    }
    public String getRatingLevel()
    {
        return M_RatingLevel;
    }
    public void setRatingLevel(String RatingLevel)
    {
        this.M_RatingLevel=RatingLevel;
    }
    public String getReleasedDate()
    {
        return M_ReleasedDate;
    }
    public void setReleasedDate(String ReleasedDate)
    {
        this.M_ReleasedDate=ReleasedDate;
    }
    public String getOverview()
    {
        return M_Overview;
    }
    public void setOverview(String Overview)
    {
        this.M_Overview=Overview;
    }
    public String getImageURL()
    {
        return M_ImageURL;
    }
    public void setImageURL(String ImageURL)
    {
        this.M_ImageURL=ImageURL;
    }
    public int getMovie_id()
    {
        return M_Movie_id;
    }
    public void setMovie_id(int Movie_id)
    {
        this.M_Movie_id=Movie_id;
    }
    public String getOriginalLang()
    {
        return M_OriginalLang;
    }
    public void setOriginalLang(String OriginalLang)
    {
        this.M_OriginalLang=OriginalLang;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(M_Title);
        parcel.writeString(M_RatingLevel);
        parcel.writeString(M_Overview);
        parcel.writeString(M_ImageURL);
        parcel.writeString(M_ReleasedDate);
        parcel.writeString(M_OriginalLang);
        parcel.writeInt(M_Movie_id);
    }



    public static final Parcelable.Creator<Movie_OBJ> CREATOR = new Parcelable.Creator<Movie_OBJ>() {
        @Override
        public Movie_OBJ createFromParcel(Parcel parcel) {
            return new Movie_OBJ(parcel);
        }

        @Override
        public Movie_OBJ[] newArray(int i) {
            return new Movie_OBJ[i];
        }

    };


}
