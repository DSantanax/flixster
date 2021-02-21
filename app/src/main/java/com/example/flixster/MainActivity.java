package com.example.flixster;

// tools

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// bundle usage
import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
// for logging
import android.util.Log;
import android.widget.LinearLayout;
// async & json handlers
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class MainActivity extends AppCompatActivity {

    // URL for the now playing end-point with the API MovieDB
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=%s";
    // Refer to this in our logs
    public static final String TAG = "MainActivity";

    // refer to activity_main binding
    private ActivityMainBinding binding;

    // Movie data, testing
    // public List<Movie> movieList;

    // Create the main screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call super method from Bundle
        super.onCreate(savedInstanceState);
        // Call the layout, use data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Define recyclerview, get it from the activity_main XML
        RecyclerView rvMovies = binding.rvMovies;
        // create the list for teh API response & the adapter
        List<Movie> movieList = new ArrayList<>();
        // create the adapter, context - activity , movieList the list of movies
        MovieAdapter movieAdapter = new MovieAdapter(this, movieList);
        // set the adapter onto the recyclerview
        rvMovies.setAdapter(movieAdapter);
        // set the layout manager onto the recyclerview
        rvMovies.setLayoutManager(new LinearLayoutManager(this));


        // create instance for async call
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        // get the data JSON format
        asyncHttpClient.get(String.format(NOW_PLAYING_URL, BuildConfig.MOVIE_API_KEY), new JsonHttpResponseHandler() {
            // when data is received
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // debugging (logging or breakpoints)
                Log.d(TAG, "onSuccess");
                // we know the data is an object (returned)
                JSONObject jsonObject = json.jsonObject;
                // key code results - is an array
                // this complains because parsing the JSON the key may N/A
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results received: " + results.toString());

                    // add all the results (movies) to the list, do not create a new movie
                    // it will point to diff. object
                    movieList.addAll(Movie.fromJsonArray(results));

                    // let the adapter know when the data changes ^ for the addAll
                    movieAdapter.notifyDataSetChanged();

                    Log.i(TAG, "Movies: " + movieList.size());
                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception");
                    e.printStackTrace();
                }

            }

            // failure
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}