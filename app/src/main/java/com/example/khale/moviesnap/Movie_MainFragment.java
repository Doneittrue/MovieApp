package com.example.khale.moviesnap;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class Movie_MainFragment extends Fragment {
    private static final String PARCELABLE_KEY="PARCELABLE_KEY";
    private static final String SORT_KEY = "sort_key";
    private static final String MOVIE_KEY = "movies";
    private GridView gridview;
    private Movie_OBJ movie;
    private ArrayList<Movie_OBJ> list;
    private static final String Sort_Setting = "sort_setting";
    private static final String Descending_Popularity = "popular";
    private static final String Descending_rating = "top_rated";
    private String FAVORITE_ITEMS="favorites";
    private String sorting_method = Descending_Popularity;
    private GridMovie_Adapter Adapter;
    private ArrayList<Movie_OBJ> MovieList;
    private String MovieKey="movies";
    private LinearLayout CautionlinearLayout;
    private Button Retry_btn;
    private wifiDetector Conn;
    private boolean isConnected = false;

    private SnapInteface mInterface;
    //Movie columns that accessed from MovieAppContract.MovieEntry
    private static final String[] MOVIE_COLUMNS = {
            SnapAppContact.MovieEntry._ID,
            SnapAppContact.MovieEntry.COLUMN_MOVIE_ID,
            SnapAppContact.MovieEntry.COLUMN_TITLE,
            SnapAppContact.MovieEntry.COLUMN_MAIN_IMAGE,
            SnapAppContact.MovieEntry.COLUMN_DESCRIPTION,
            SnapAppContact.MovieEntry.COLUMN_RATING,
            SnapAppContact.MovieEntry.COLUMN_ORIGINAL_LANGUAGE,
            SnapAppContact.MovieEntry.COLUMN_RELEASED_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_MAIN_IMAGE = 3;
    public static final int COL_DESCRIPTION = 4;
    public static final int COL_RATING = 5;
    public static final int COL_ORGINAIL_LANGUAGE = 6;
    public static final int COL_RELEASED_DATE = 7;


    public Movie_MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MovieKey)) {
            //initailizing the movie list
            MovieList = new ArrayList<Movie_OBJ>();
            //Log for testing
            Log.d("intro","inside if statement");
        }
        else {
            //initailizing the movie list
            MovieList = savedInstanceState.getParcelableArrayList(MovieKey);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_movie_main, container, false);





        //defining an object of ConDetection and give it Context to initailize it constructor
        Conn = new wifiDetector(getActivity());
        //check if there an internet connection
        isConnected = Conn.isConnectingToInternet();

        //casting grid view
        gridview=(GridView)rootview.findViewById(R.id.grid_main);
        CautionlinearLayout=(LinearLayout) rootview.findViewById(R.id.CautionLinearLayout);
        Retry_btn=(Button)rootview.findViewById(R.id.retry);


        //updateMovieData(sorting_method);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie_OBJ movie_obj=Adapter.getItem(position);

                if(movie_obj==null)
                {Log.i("eweqssdsa","movie is null");}else {Log.i("eweqssdsa","movie is not null");}

                //Toast.makeText(getActivity(),"my position is : "+position,Toast.LENGTH_LONG).show();
//                Intent i = new Intent(getActivity(), DetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(PARCELABLE_KEY,movie_obj);
//                i.putExtras(bundle);
//                getActivity().startActivity(i);

                mInterface.MovieChoosed(movie_obj);
            }
        });


        if (isConnected) {
            //there is an internet connection
            //we set the lineaner layout to invisible and the gridview to visible
            CautionlinearLayout.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            updateMovieData(sorting_method);
            isConnected = false;
        }else
        {
            //there is an internet connection
            //we set the lineaner layout to visible and the gridview to invisible
            CautionlinearLayout.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.GONE);
            //listener when the retry button clicked
            Retry_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if there an internet connetion
                    if(Conn.isConnectingToInternet())
                    {
                        //we set the lineaner layout to invisible and the gridview to visible
                        CautionlinearLayout.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                        updateMovieData(sorting_method);
                        isConnected = false;
                    }else{
                        //toast messge when the wifi doesnot enabled
                        //Toast.makeText(getActivity(),"You should Enable Your WIFI Connection ",Toast.LENGTH_LONG).show();
                        Snackbar.make(view, "You Should Enable Your WIFI Connection", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }


        return rootview;
    }


//    private void updateMovieData(String sorting_method)
//    {
//        new FetchMovieTask().execute(sorting_method);
//
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!sorting_method.contentEquals(Descending_Popularity)) {
            outState.putString(SORT_KEY, sorting_method);
        }
        if (MovieList != null) {
            outState.putParcelableArrayList(MOVIE_KEY, MovieList);
        }
        super.onSaveInstanceState(outState);
    }

    // update weather function
    private void updateMovieData(String sorting_method)
    {

        if (!sorting_method.contentEquals(FAVORITE_ITEMS)) {
            //to fetch movies sorted by popularity or by rating level
            new FetchMovieTask().execute(sorting_method);
        } else {
            //to fetch favorited movie
            new FetchMoviesFavoratedTask(getActivity()).execute();
        }

    }
    //async task to get the Movie Task
    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie_OBJ>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        //get array list of movies
        public ArrayList<Movie_OBJ> getMovieDataFromJson(String movieJsonString)
                throws JSONException {
            //Log for testing
            Log.d("inside","hello getWeatherDataFromJson");

            // These are the names of the JSON objects that need to be extracted.
            final String RESULT = "results";

            JSONObject movieObJson = new JSONObject(movieJsonString);
            JSONArray movieArray = movieObJson.getJSONArray(RESULT);

            //initailizing the list
            list = new ArrayList<Movie_OBJ>();

            for(int i = 0; i < movieArray.length(); i++) {
                //initailizing the movie object
                movie= new Movie_OBJ("","","","","","",0);
                // Get the JSON object representing the movie
                JSONObject movieObj = movieArray.getJSONObject(i);

                final String POSTER_PATH="poster_path";
                final String RELEASE_DATE="release_date";
                final String OVERVIEW="overview";
                final String VOTE_AVERAGE="vote_average";
                final String ORIGINAL_TITLE="original_title";
                final String ORIGINAL_LANGUAGE="original_language";
                final String ID="id";
                //setting the movie object
                movie.setImageURL(movieObj.getString(POSTER_PATH));
                movie.setReleasedDate(movieObj.getString(RELEASE_DATE));
                movie.setOverview(movieObj.getString(OVERVIEW));
                movie.setRatingLevel(movieObj.getString(VOTE_AVERAGE));
                movie.setTitle(movieObj.getString(ORIGINAL_TITLE));
                movie.setOriginalLang(movieObj.getString(ORIGINAL_LANGUAGE));
                movie.setMovie_id(movieObj.getInt(ID));
                list.add(movie);
            }

            return list;
        }
        @Override
        protected ArrayList<Movie_OBJ> doInBackground(String... params) {

            Log.d("inside","hello in don in background");

            //   Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String format = "json";

            try {
                // Construct the URL
                final String FORECAST_BASE_URL =
                        "https://api.themoviedb.org/3/movie/"+params[0]+"?";
                //key for the api
                final String APPID_PARAM = "api_key";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_APP_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to MovieAPI, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Movie string: " + movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);

                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the data.
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)

        @Override
        protected void onPostExecute(ArrayList<Movie_OBJ> result) {
            if (result != null) {

                Log.d("intro","inside onpostexecute");
                MovieList=result;
                //setting the adapter and the gridview
                Adapter= new GridMovie_Adapter(getActivity(),MovieList);
                gridview.setAdapter(Adapter);

                // New data is back from the server.  Hooray!
                Log.d("introasasas","inside onpostexecute");
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie__main, menu);
        //casting popularity option
        MenuItem action_popularity = menu.findItem(R.id.group_item_popularity);
        //casting rated level option
        MenuItem action_rating_level = menu.findItem(R.id.group_item_rating);
        //casting favourite option
        MenuItem action_Favorite = menu.findItem(R.id.group_item_rating);

        //when we need top filter results by popularity
        if (sorting_method.contentEquals(Descending_Popularity)) {
            //if popularity option dosenot checked
            //check it
            if (!action_popularity.isChecked()) {
                action_popularity.setChecked(true);
            }
        } else if (sorting_method.contentEquals(Descending_rating)) {
            //if rating option dosenot checked
            //check it
            if (!action_rating_level.isChecked()) {
                action_rating_level.setChecked(true);
            }
        }else if(sorting_method.contentEquals(FAVORITE_ITEMS))
        {
            //if favourite option dosenot checked
            //check it
            if(!action_Favorite.isChecked())
            {
                action_Favorite.setChecked(true);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            //sorting by the most popular
            case R.id.group_item_popularity:
                //if populaity checked make it un checked
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                //setting the sorting method
                sorting_method = Descending_Popularity;
                updateMovieData(sorting_method);
                return true;

            //sorting by the most rated
            case R.id.group_item_rating:
                //if rating checked make it un checked
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                //setting the sorting method
                sorting_method = Descending_rating;
                updateMovieData(sorting_method);
                return true;
            // favorites
            case R.id.group_item_favorite:
                //if favorite checked make it un checked
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                //setting the sorting methods
                sorting_method = FAVORITE_ITEMS;
                updateMovieData(sorting_method);
                return true;

            // default option
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //async task to get favourited movie
    public class FetchMoviesFavoratedTask extends AsyncTask<Void, Void, ArrayList<Movie_OBJ>> {

        //defining the context
        private Context mContext;

        //the constructor initailizing the context
        public FetchMoviesFavoratedTask(Context context) {
            mContext = context;
        }

        //get movie data from the cursor
        private ArrayList<Movie_OBJ> getMoviesFavoritedDataFromCursor(Cursor cursor) {
            //defining and initializing the arralist of results
            ArrayList<Movie_OBJ> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    //take every movie object an put it in results arraylist
                    Movie_OBJ movie = new Movie_OBJ(cursor);
                    results.add(movie);
                } while (cursor.moveToNext());
                //we close the cursor here
                cursor.close();
            }
            //then we return the results.....
            return results;
        }

        //method running in the background .....
        @Override
        protected ArrayList<Movie_OBJ> doInBackground(Void... params) {
            //defining the cursor objest then we use the content resolver to deal with content provider
            Cursor cursor = mContext.getContentResolver().query(
                    SnapAppContact.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            //then we return the data from the cursor
            return getMoviesFavoritedDataFromCursor(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie_OBJ> movies) {
            if (movies != null) {
                if (Adapter != null) {
                    //setting the movies into MovieList
                    MovieList=movies;
                    //initailizing the Adapter and the grid view ...
                    Adapter= new GridMovie_Adapter(getActivity(),MovieList);
                    gridview.setAdapter(Adapter);
                }

            }
        }
    }

    // set the value for the MovieInterface object
    public void setMovieInterface(SnapInteface Interface)
    {
        mInterface=Interface;
    }
}
