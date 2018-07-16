package com.example.khale.moviesnap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by khale on 10/27/2016.
 */

public class GridMovie_Adapter extends ArrayAdapter<Movie_OBJ> {

    // constructor with argument for context
    private Context context;
    //defining array list of movies
    private ArrayList<Movie_OBJ> Movie1;
    public GridMovie_Adapter (Activity context, ArrayList<Movie_OBJ> Movie)
    {
        //setting the context and movie array list to its super class
        super(context, 0, Movie);
        //initializing the context and the movie arraylist
        this.context=context;
        this.Movie1=Movie;

    }

    //api key
    //1acfa866db0152280781e2943e41a93e

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie_OBJ Movie = getItem(position);
        //image view that displayed in the grid view
        ImageView imageView = new ImageView(getContext());
        //when the convert view dosent equal null then the adapter create new view for it.
        if(convertView!=null) {
            //set image view properities
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
            //setting the iamge url
            String imageUrl = "http://image.tmdb.org/t/p/w185/" + Movie.getImageURL();
            //Log for testing
            Log.d("intro", imageUrl);
            //picasso library loading the loading image first until it can load the movie image
            Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.loading_process).into(imageView);

            return imageView;

        }else{
            return imageView;
        }



    }


}
