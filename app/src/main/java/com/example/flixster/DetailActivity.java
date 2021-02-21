package com.example.flixster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Locale;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    //    URL endpoint for videos, format the ID %d for decimal
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=%s&language=en-US";

    //    references to the views
    TextView tvTitleDetail;
    TextView tvOverviewDetail;
    RatingBar ratingBarDetail;

    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        get the views
        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvOverviewDetail = findViewById(R.id.tvOverviewDetail);
        ratingBarDetail = findViewById(R.id.ratingBarDetail);
        youTubePlayerView = findViewById(R.id.player);

        // receive the data (testing)
//        String title = getIntent().getStringExtra("movie");

        // receive the parceler object
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitleDetail.setText(movie.getTitle());
        tvOverviewDetail.setText(movie.getOverView());

//        set rating, downcast the double into a float,
//        set rating expects a float
        ratingBarDetail.setRating((float) movie.getMovieRating());

//        set the title (testing)
//        tvTitleDetail.setText(title);

        // use http client to get the data
        AsyncHttpClient client = new AsyncHttpClient();
        // get request, (URL, res handler)
        client.get(String.format(Locale.US, VIDEOS_URL, movie.getMovieId(), BuildConfig.MOVIE_API_KEY), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
//                    no videos available, return
                    if (results.length() == 0) {
                        // can add a placeholder image instead (background)
                        return;
                    }
                        // get the first video, we can also check if the site is YT
                        String youtube_key = results.getJSONObject(0).getString("key");
                        initializeYoutube(youtube_key);
//                        log key
//                        Log.d("DetailActivity", youtube_key);
                } catch (JSONException e) {
                    Log.e("DetailActivity", "Failed to parse JSON detail");
                    e.printStackTrace();
                }

            }
//            method to call yt player
            private void initializeYoutube(final String youtube_key) {
//        create a yt video (key, listener)
                youTubePlayerView.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                    //            on success
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        Log.d("Detail", "onSuccess");
//                auto play the video ( video ID)
                        if(movie.getMovieRating() > 5){
                            youTubePlayer.loadVideo(youtube_key);
                        }
                        else {
                            // user plays the video
                            youTubePlayer.cueVideo(youtube_key);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d("Detail", "onFailure");
                    }
                });
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            // allow back transition
            finishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}