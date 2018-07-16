package com.example.khale.moviesnap;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.ShareActionProvider;

import com.squareup.picasso.Picasso;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    public static final String PARCELABLE_KEY="PARCELABLE_KEY";
    private ImageView ImageMovie;
    private TextView Title;
    private TextView Rating;
    private TextView Language;
    private TextView LunchedDate;
    private String Language_short;
    private RatingBar rate_bar ;
    private TextView Overview;
    private Movie_OBJ movie_obj;
    private ScrollView DetailMovie;
    private ListView TrailerList;
    private Trailer trailer;
    private ListView ReviewList;
    private ArrayList<Trailer> listTrailer;
    private TrailerAdapter trailersAdapter;
    private TextView TrailerMessage;
    private TextView ReviewMessage;
    private Review review;
    private ArrayList<Review> listReview;
    private ReviewAdapter reviewAdapter;
    private CheckBox FavoriteButton;
    private static final String  MOVIE_SHARE_HASHTAG="#Movie Snap";
    private Toast toast;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);


        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            //get movie object
            movie_obj=bundle.getParcelable(PARCELABLE_KEY);
        }else{
            Log.i("sadasdcvds","bundle is null");
        }
        //posting the data into the View
        //posting the image

        //casting the Movie Scroll View
        DetailMovie = (ScrollView) rootView.findViewById(R.id.detailScrollView);
        //determinig the visibility of ScrollView
        DetailMovie.setVisibility(View.VISIBLE);

        //posting the data into the View
        //posting the image
        ImageMovie = (ImageView) rootView.findViewById(R.id.detailImage);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/"+movie_obj.getImageURL()).into(ImageMovie);
        //testing using LOG
        Log.d("fffoiiihhhh",movie_obj.getImageURL());

        //posting the title
        Title=(TextView)rootView.findViewById(R.id.title);
        Title.setText(movie_obj.getTitle());

        //posting rating level or degree
        Rating=(TextView)rootView.findViewById(R.id.RatingTxt);
        Rating.setText(movie_obj.getRatingLevel()+"/10");

        //posting rating level by the stars
        rate_bar=(RatingBar)rootView.findViewById(R.id.ratingBar);
        rate_bar.setRating(Float.parseFloat(movie_obj.getRatingLevel()));

        //posting the lunched date
        LunchedDate=(TextView)rootView.findViewById(R.id.LunchedDateValue);
        LunchedDate.setText(movie_obj.getReleasedDate());

        //posting the original language
        Language=(TextView)rootView.findViewById(R.id.langValue);

        if(movie_obj.getOriginalLang() == "en")
        {
            Language_short="English";
        }else if(movie_obj.getOriginalLang() == "ar")
        {
            Language_short="Arabic";
        }else
        {
            Language_short="English";
        }

        Language.setText(Language_short);

        //posting review data

        Overview=(TextView)rootView.findViewById(R.id.Reviewtxt);
        Overview.setText("    "+movie_obj.getOverview());


        //favorite button listener
        FavoriteButton = (CheckBox)rootView.findViewById(R.id.favorite);

        if(movie_obj!=null)
        {

            // to check if the movie was favorited or not
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return SnapUtility.isChecked(getActivity(), movie_obj.getMovie_id());
                }

                @Override
                protected void onPostExecute(Integer isChecked) {

                    Log.i("kokokoko",""+isChecked);

                    if(isChecked==1)
                    {
                        FavoriteButton.setChecked(true);
                    }else
                    {
                        FavoriteButton.setChecked(false);
                    }
                }
            }.execute();
        }

        FavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (movie_obj != null) {
                    // check if the movie  favorited or not
                    new AsyncTask<Void, Void, Integer>() {

                        @Override
                        protected Integer doInBackground(Void... params) {
                            return SnapUtility.isChecked(getActivity(), movie_obj.getMovie_id());
                        }

                        @Override
                        protected void onPostExecute(Integer Check) {
                            // if it is in favorites
                            if (Check == 1) {
                                // delete from favorite
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(
                                                SnapAppContact.MovieEntry.CONTENT_URI,
                                                SnapAppContact.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                                new String[]{Integer.toString(movie_obj.getMovie_id())}
                                        );
                                    }

                                    @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        FavoriteButton.setChecked(false);
                                        if (toast != null) {
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(getActivity(), getString(R.string.unFavorite), Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            }
                            // if it is not in favorites
                            else {
                                // add to favorites

                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();
                                        // inserting value of movie object
                                        values.put(SnapAppContact.MovieEntry.COLUMN_MOVIE_ID, movie_obj.getMovie_id());
                                        values.put(SnapAppContact.MovieEntry.COLUMN_TITLE, movie_obj.getTitle());
                                        values.put(SnapAppContact.MovieEntry.COLUMN_MAIN_IMAGE, movie_obj.getImageURL());
                                        values.put(SnapAppContact.MovieEntry.COLUMN_DESCRIPTION, movie_obj.getOverview());
                                        values.put(SnapAppContact.MovieEntry.COLUMN_RATING, movie_obj.getRatingLevel());
                                        values.put(SnapAppContact.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie_obj.getOriginalLang());
                                        values.put(SnapAppContact.MovieEntry.COLUMN_RELEASED_DATE, movie_obj.getReleasedDate());

                                        return getActivity().getContentResolver().insert(SnapAppContact.MovieEntry.CONTENT_URI,
                                                values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        FavoriteButton.setChecked(true);
                                        if (toast != null) {
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(getActivity(),getString(R.string.Favorite), Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }

            }

        });


        // List for trailers
        TrailerList=(ListView)rootView.findViewById(R.id.trailer_content);  // your listview inside scrollview

        //allow the Trailer to be scrollable
        TrailerList.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        //listener when the trailer clicked
        TrailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                trailer= trailersAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //set the url to get the  intro image of video
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getTrailer_key()));
                startActivity(intent);
            }
        });

        // casting the trailer message
        TrailerMessage= (TextView) rootView.findViewById(R.id.trailer_message);
        if(trailer==null)
        {
            //make message visible if  TrailerList equal null
            TrailerMessage.setVisibility(View.VISIBLE);

        }else{
            //make message invisible if  TrailerList not equal null
            TrailerMessage.setVisibility(View.GONE);
        }

        //casting the review list
        ReviewList=(ListView)rootView.findViewById(R.id.review_content);
        // allow the review list to be scrollable
        ReviewList.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        // casting the trailer message
        ReviewMessage= (TextView) rootView.findViewById(R.id.Review_message);
        if(review==null)
        {
            //make message visible if  TrailerList equal null
            ReviewMessage.setVisibility(View.VISIBLE);

        }else{
            //make message invisible if  TrailerList not equal null
            ReviewMessage.setVisibility(View.GONE);
        }


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (movie_obj != null) {
            //to fetch trailer when app start life cycle
            new FetchTrailerTask().execute(Integer.toString(movie_obj.getMovie_id()));
            //to fetch review when app start life cycle
            new FetchReviewsTask().execute(Integer.toString(movie_obj.getMovie_id()));
        }
    }

    // the trailer async task that fetch trailer and pass data to the Adapter
    public class FetchTrailerTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();



        //get the trailer Array list
        public ArrayList<Trailer> getMovieDataFromJson(String movieJsonString)
                throws JSONException {
            //trailer Log test
            Log.d("inside","hello getWeatherDataFromJson");

            // These are the names of the JSON objects that need to be extracted.
            final String RESULT = "results";
            JSONObject movieObJson = new JSONObject(movieJsonString);
            JSONArray movieArray = movieObJson.getJSONArray(RESULT);
            //initialize the list trailer
            listTrailer = new ArrayList<Trailer>();

            for(int i = 0; i < movieArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                trailer= new Trailer("","","","","");
                // Get the JSON object representing the trailers
                JSONObject movieObj = movieArray.getJSONObject(i);
                //setting the trailer object
                final String ID="id";
                final String KEY="key";
                final String NAME="name";
                final String SITE="site";
                final String TYPE="type";
                trailer.setTrailer_Id(movieObj.getString(ID));
                trailer.setTrailer_key(movieObj.getString(KEY));
                trailer.setTrailer_Name(movieObj.getString(NAME));
                trailer.setTrailer_Site(movieObj.getString(SITE));
                trailer.setTrailer_Type(movieObj.getString(TYPE));
                listTrailer.add(trailer);
            }
            //testing Log
            Log.i("appps",movieArray.length()+"");

            return listTrailer;
        }

        //method run on the background...
        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {

            //testing LOG
            Log.d("inside","hello in don in background");

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                // Construct the URL
                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/movie/"+params[0]+"/videos"+"?";


                // define the key for the api
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_APP_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                //log message the shows the uri
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to MOVIE api, and open the connection
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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
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
                        //log to notify that an error that closed the stream
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

        //performed after the doingbackground finished ..
        @Override
        protected void onPostExecute(ArrayList<Trailer> result) {
            if (result != null) {

                //setting the adapter and the trailer list
                trailersAdapter=new TrailerAdapter(getActivity(),result);
                TrailerList.setAdapter(trailersAdapter);

            }

        }
    }

    // the reviews async task that fetch review and pass data to the Adapter
    public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

        private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();

        //get the list of reviews
        public List<Review> getMovieDataFromJson(String movieJsonString)
                throws JSONException {
            //log for testing...
            Log.d("inside","hello getWeatherDataFromJson");

            // These are the names of the JSON objects that need to be extracted.
            final String RESULT = "results";
            JSONObject movieObJson = new JSONObject(movieJsonString);
            JSONArray movieArray = movieObJson.getJSONArray(RESULT);

            //initializing the list reviews
            listReview = new ArrayList<Review>();

            for(int i = 0; i < movieArray.length(); i++) {
                //initializing the review object
                review= new Review("","","","");
                // get the JSON object representing the review
                JSONObject movieObj = movieArray.getJSONObject(i);
                //setting the review object
                final String ID="id";
                final String AUTHOR="author";
                final String CONTENT="content";
                final String URL="url";
                review.setReview_Id(movieObj.getString(ID));
                review.setReview_Autor(movieObj.getString(AUTHOR));
                review.setReview_Content(movieObj.getString(CONTENT));
                review.setReview_URL(movieObj.getString(URL));
                listReview.add(review);

                //log for testing
                Log.i("apppsppp",trailer.getTrailer_Name());

            }

            return listReview;
        }
        //method run on the background...
        @Override
        protected List<Review> doInBackground(String... params) {

            //log for testing
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

            try {

                //defining the base URL
                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/movie/"+params[0]+"/reviews"+"?";
                //defining the key for api
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_APP_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                //Log for shows the URI
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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
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
        protected void onPostExecute(List<Review> result) {
            if (result != null) {
                //setting the adapter and the review list.....
                reviewAdapter=new ReviewAdapter(getActivity(),result);
                ReviewList.setAdapter(reviewAdapter);
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.properities, menu);
        super.onCreateOptionsMenu(menu,inflater);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        } else {
            //Log for testing
            Log.d("inside", "Share Action Provider is null?");
        }



    }

    //for share your movie
    private Intent createShareMovieIntent() {
        //start implicit Intent with action send to share text and image.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //defining the information that we want to share
        Uri ImageURI =  Uri.parse("http://image.tmdb.org/t/p/w185/"+movie_obj.getImageURL());
        String Title = movie_obj.getTitle();
        String RatingLevel = movie_obj.getRatingLevel();
        String ReleasedDate = movie_obj.getReleasedDate();
        String Overview = movie_obj.getOverview();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, RatingLevel);
        shareIntent.putExtra(Intent.EXTRA_TEXT, ReleasedDate);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Language_short);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Overview+"   "+MOVIE_SHARE_HASHTAG);
        shareIntent.putExtra(Intent.EXTRA_STREAM,ImageURI);
        //to share image and text
        shareIntent.setType("image/*");
        //set the permission
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return shareIntent;
    }

}
