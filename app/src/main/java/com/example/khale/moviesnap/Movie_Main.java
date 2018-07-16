package com.example.khale.moviesnap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class Movie_Main extends AppCompatActivity  implements SnapInteface{

    /**the flag that determine
     if the device in phone mode or in tablet mode
     */
    private Boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__main);

        // casting the detail frame layout
        FrameLayout frame_detail=(FrameLayout)findViewById(R.id.frame_detail);

        if(frame_detail == null)
        {
            //single pane (phone mode)
             mTwoPane=false;
            Log.i("ahmadasdkashdlksad","one pane");
        }
        else
        {
            //two pane (tablet mode)
            mTwoPane=true;
            Log.i("ahmadasdkashdlksad","two pane");
        }




        if(savedInstanceState==null)
        {
            //define new object DetailActivityFragment
            Movie_MainFragment MainFragment=new Movie_MainFragment();
            //setting the interface
            MainFragment.setMovieInterface(this);
            //inflate the Detail fragment
            getFragmentManager().beginTransaction().replace(R.id.frame_main, MainFragment).commit();

        }
    }

    //the method that overrided because we implement the interface (Moview interface)
    //this method accept a movie object
    @Override
    public void MovieChoosed(Movie_OBJ  movie_obj) {

        if(mTwoPane)
        {
            Log.i("ahmadasdkashdlksadw","two pane");

            //case two pane ui
            DetailActivityFragment DetailFragment= new DetailActivityFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable(DetailFragment.PARCELABLE_KEY,movie_obj);
            DetailFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_detail, DetailFragment).commit();

        }
        else{
            //case single pane ui
            Intent i = new Intent(this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailActivityFragment.PARCELABLE_KEY,movie_obj);
            i.putExtras(bundle);
            this.startActivity(i);

        }

    }
}
